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

@Service
class FriendService(private val friendRepo: FriendRepository) {
    private val LOGGER = LoggerFactory.getLogger(FriendService::class.java)
    fun friendSizeByUser(user: User): Int {
        return friendRepo.countFriends(user)
    }

    fun getFriendList(user: User): UserListResponse {
        val users = friendRepo.findUsersByUserAndState(user, FriendState.ACCEPTED)
        if (users.isEmpty())
            throw CustomError(ErrorStatus.USER_DO_NOT_HAVE_FRIEND)
        val userMapNotMe = users.map { if (it.sender == user) it.receiver else it.sender }

        return UserListResponse(userMapNotMe.map { UserIdAndNameResponse(it.id.toString(), it.name) })
    }

    fun getPending(user: User): List<PendingResponse> {
        val invitedUsers = friendRepo.findUsersByUserAndState(user)
        if (invitedUsers.isEmpty())
            throw CustomError(ErrorStatus.USER_DO_NOT_HAVE_FRIEND)
        return invitedUsers.map {
            PendingResponse(
                UserIdAndNameResponse(
                    it.sender.id.toString(),
                    it.sender.name
                ),
                UserIdAndNameResponse(
                    it.receiver.id.toString(),
                    it.receiver.name
                )
            )
        }
    }


    fun inviteFriend(sender: User, receiver: User) {
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
