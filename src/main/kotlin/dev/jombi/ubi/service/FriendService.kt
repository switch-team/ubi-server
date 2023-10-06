package dev.jombi.ubi.service;

import dev.jombi.ubi.dto.response.UserIdAndNameResponse
import dev.jombi.ubi.dto.response.UserListResponse
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.UserRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorDetail
import org.springframework.stereotype.Service

@Service
class FriendService(val userRepository: UserRepository) {
    fun findUser(phoneOrEmail: String): UserIdAndNameResponse {
        val user = (
                if (phoneOrEmail.contains("@")) userRepository.getUserByEmail(phoneOrEmail)
                else userRepository.getUserByPhone(phoneOrEmail.replace("-", ""))
                ) ?: throw CustomError(ErrorDetail.USER_NOT_FOUND)
        return UserIdAndNameResponse(id = user.phone, name = user.name)
    }

    fun getFriendList(user: User): UserListResponse {
        if (user.friend.isEmpty()) throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_FRIEND)
        return UserListResponse(user.friend.map { UserIdAndNameResponse(it.phone, it.name) })
    }

    fun getInvitedList(user: User): UserListResponse {
        if (user.invite.isEmpty()) throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_FRIEND)
        return UserListResponse(user.invite.map { UserIdAndNameResponse(it.phone, it.name) })
    }

    fun getReceivedList(user: User): UserListResponse {
        if (user.receive.isEmpty()) throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_FRIEND)
        return UserListResponse(user.receive.map { UserIdAndNameResponse(it.phone, it.name) })
    }


    // inviteduser 친추받는 사람 user 나
    fun inviteFriend(invitedUser: User, user: User) {
        if (invitedUser.invite.contains(user)) {
            this.acceptInviteFriend(invitedUser, user)
        } else {
            user.invite.add(invitedUser)
            invitedUser.receive.add(user)
            userRepository.save(user)
            userRepository.save(invitedUser)
        }
    }

    fun acceptInviteFriend(invitedUser: User, user: User) {
        if (invitedUser.invite.contains(user)) {
            user.friend.add(invitedUser)
            invitedUser.friend.add(user)
            invitedUser.invite.remove(user)
            user.receive.remove(invitedUser)
            userRepository.save(user)
            userRepository.save(invitedUser)
        } else {
            throw CustomError(ErrorDetail.USER_NOT_INVITED)
        }
    }

    fun rejectInviteFriend(invitedUser: User, user: User) {
        if (invitedUser.invite.contains(user)) {
            invitedUser.invite.remove(user)
            user.receive.remove(invitedUser)
            userRepository.save(user)
            userRepository.save(invitedUser)
        } else {
            throw CustomError(ErrorDetail.USER_DO_NOT_HAVE_FRIEND)
        }
    }

//    fun inviteFriend(invitedUser: User, user: User) {
//        invitedUser.waitFriends.add(user)
//        user.invitedFriends.add(invitedUser)
//        userRepository.save(invitedUser)
//        userRepository.save(user)
//    }
//
//    fun reciveFriend(recivedUser: User, user: User) {
//        user.waitFriends.contains(recivedUser)
//        user.friend.add(recivedUser)
//        user.waitFriends.remove(recivedUser)
//    }

}
