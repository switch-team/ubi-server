package dev.jombi.ubi.service

import dev.jombi.ubi.dto.response.PendingResponse
import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.dto.response.UserListResponse
import dev.jombi.ubi.entity.Friend
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.FriendRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import dev.jombi.ubi.util.state.FriendState
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FriendService(private val friendRepo: FriendRepository) {
    private val LOGGER = LoggerFactory.getLogger(FriendService::class.java)
    fun friendSizeByUser(user: User): Int {
        return friendRepo.countFriends(user)
    }

    fun isUserFriend(user: User, target: UUID): Boolean {
        return (friendRepo.findRelationshipById(user, target) ?: return false).state == FriendState.ACCEPTED
    }

    fun getFriendList(user: User): UserListResponse {
        val users = friendRepo.findUsersByUserAndState(user, FriendState.ACCEPTED)
        if (users.isEmpty())
            return UserListResponse(emptyList())
        val userMapNotMe = users.map { if (it.sender == user) it.receiver else it.sender }

        return UserListResponse(userMapNotMe.map { UserIdAndNameResponse(it.id.toString(), it.name) })
    }

    fun getPending(user: User): List<UserIdAndNameResponse> {
        val invitedUsers = friendRepo.findPendingRequests(user)
//        if (invitedUsers.isEmpty())
//            throw CustomError(ErrorStatus.USER_DO_NOT_HAVE_FRIEND)
        return invitedUsers.map {
            UserIdAndNameResponse(
                it.sender.id.toString(),
                it.sender.name
            )
        }
    }


    fun inviteFriend(sender: User, receiver: User) {
        if (sender.id == receiver.id)
            throw CustomError(ErrorStatus.NO_SELF_CONFIRM)
        val n = friendRepo.findFriendByTwoUser(sender, receiver)
            ?: return friendRepo.save(Friend(sender = sender, receiver = receiver, state = FriendState.PENDING)).let {}
        if (n.receiver == sender) acceptFriendRequest(receiver, sender)
        throw CustomError(ErrorStatus.FRIEND_REQUEST_ALREADY_SENT)
    }

    fun acceptFriendRequest(receiver: User, sender: User) {
        LOGGER.info("{} {}", receiver, sender)
        val n = friendRepo.findFriendByTwoUser(receiver, sender)
            ?: throw CustomError(ErrorStatus.USER_IS_NOT_FRIEND)
        if (n.sender == receiver)
            throw CustomError(ErrorStatus.NO_SELF_CONFIRM)
        if (n.state == FriendState.ACCEPTED)
            throw CustomError(ErrorStatus.USER_IS_ALREADY_FRIEND)
        friendRepo.save(n.copy(state = FriendState.ACCEPTED))
    }

    fun deleteFriend(sender: User, receiver: User) {
        val n = friendRepo.findFriendByTwoUser(sender, receiver)
            ?: throw CustomError(ErrorStatus.USER_IS_NOT_FRIEND)
        friendRepo.delete(n)
    }
}
