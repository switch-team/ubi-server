package dev.jombi.ubi.service

import dev.jombi.ubi.entity.Assemble
import dev.jombi.ubi.entity.AssembleUser
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.mongo.AssembleRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import dev.jombi.ubi.util.state.InviteStatus
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AssembleService(
    private val assembleRepo: AssembleRepository,
    private val friend: FriendService
) {
    fun getAssembleInfo(id: UUID): Assemble {
        return assembleRepo.queryAssembleByHostIs(id) // id가 연 호스트 찾기
            ?: throw CustomError(ErrorStatus.NO_ASSEMBLE_ROOM)
    }

    fun inviteAssemble(user: User, target: User, message: String) {
        val assemble = getAssembleInfo(user.id)
        if (assemble.host == target.id || assemble.users.any { it.user == target.id })
            throw CustomError(ErrorStatus.USER_ALREADY_JOINED_ASSEMBLE)
        if (!friend.isUserFriend(user, target))
            throw CustomError(ErrorStatus.USER_IS_NOT_FRIEND)
        assembleRepo.save(assemble.let {
            it.copy(
                users = it.users + AssembleUser(
                    target.id,
                    message,
                    InviteStatus.PENDING
                )
            )
        })
    }

    fun replyAssemble(user: User, assembleId: UUID, isAccepted: Boolean) {
        val info = getAssembleInfo(assembleId)
        val n = info.users.find { it.user == user.id } ?: throw CustomError(ErrorStatus.NOT_ASSEMBLE_MEMBER)
        if (n.status != InviteStatus.PENDING)
            throw CustomError(ErrorStatus.ALREADY_ANSWERED)
        assembleRepo.save(info.copy(users = (info.users - n) + n.copy(status = InviteStatus.ACCEPTED)))
    }

//    fun getAssembleListOnUser(id: UUID): List<Assemble> {
//        val assembles: List<Assemble> = assembleRepo.queryAssemblesByUsersInUserUUID(id)
//        assembles.map { it.users.map { it.status } }.
//    }
}