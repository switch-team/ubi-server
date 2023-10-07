package dev.jombi.ubi.service;

import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.dto.response.UserListResponse
import dev.jombi.ubi.entity.Friend
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.FriendRepository
import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import dev.jombi.ubi.util.state.FriendState
import org.springframework.stereotype.Service

@Service
class FriendService(val userRepository: UserRepository, val friendRepo: FriendRepository) {
    fun getFriendList(user: User): UserListResponse {
        val users = friendRepo.findUsersByUser(user)
        if (users.isEmpty())
            throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_FRIEND)
        val userMapNotMe = users.map { if (it.sender == user) it.receiver else it.sender }
        // 나를 제외하기 위해 (친구만 찾기 위해)

        return UserListResponse(userMapNotMe.map { UserIdAndNameResponse(it.phone, it.name) })
    }

    fun getInvitedList(user: User): UserListResponse {
        val invitedUsers = friendRepo.findFriendBySenderAndStateIs(user)
        if (invitedUsers.isEmpty())
            throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_FRIEND)
        val notMe = invitedUsers.map { it.receiver }
        return UserListResponse(notMe.map { UserIdAndNameResponse(it.phone, it.name) })
    }

    fun getReceivedList(user: User): UserListResponse {
        val receivedUsers = friendRepo.findFriendByReceiverAndStateIs(user)
        if (receivedUsers.isEmpty())
            throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_FRIEND)
        val senders = receivedUsers.map { it.sender }
        return UserListResponse(senders.map { UserIdAndNameResponse(it.phone, it.name) })
    }


    fun inviteFriend(sender: User, receiver: User): Unit {
        val n = friendRepo.findFriendByTwoUser(sender, receiver)
            ?: return friendRepo.save(Friend(sender = sender, receiver = receiver, state = FriendState.PENDING)).let {}
        if (n.receiver == sender) acceptFriendRequest(receiver, sender)
        throw CustomError(ErrorDetail.ALREADY_SENT)
    }
    // user: 요청받은 사람
    fun acceptFriendRequest(receiver: User, sender: User) {
        val n = friendRepo.findFriendByTwoUser(receiver, sender)
            ?: throw CustomError(ErrorDetail.USER_NOT_INVITED)
        if (n.receiver != receiver)
            throw CustomError(ErrorDetail.NO_SELF_CONFIRM)
        friendRepo.save(n.copy(state = FriendState.ACCEPTED))
    }

    fun deleteFriend(sender: User, receiver: User) {
        val n = friendRepo.findFriendByTwoUser(sender, receiver)
            ?: throw CustomError(ErrorDetail.USER_NOT_INVITED)
        friendRepo.delete(n)
    }
}
