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
    private fun getAssembleInfo(id: UUID): Assemble {
        return assembleRepo.queryAssembleByHostIs(id)
            ?: throw CustomError(ErrorStatus.NO_ASSEMBLE_ROOM)
    }

    fun getAssembleInfoById(user: User, id: UUID): Assemble {
        if (user.id != id && !friend.isUserFriend(user, id)) // self get or friend get
            throw CustomError(ErrorStatus.USER_IS_NOT_FRIEND)
        return getAssembleInfo(id)
    }

    fun getRelatedAssembles(id: UUID) = assembleRepo.queryRelatedAssemble(id)

    fun createAssemble(user: User, title: String): Assemble {
        assembleRepo.queryAssembleByHostIs(user.id)?.let { assembleRepo.deleteById(it.host) }
        return assembleRepo.save(Assemble(host = user.id, title = title, users = emptyList()))
    }

    fun inviteAssemble(user: User, target: UUID, message: String): Assemble {
        val assemble = getAssembleInfo(user.id)
        if (assemble.host == target || assemble.users.any { it.user == target })
            throw CustomError(ErrorStatus.USER_ALREADY_JOINED_ASSEMBLE)
        if (!friend.isUserFriend(user, target))
            throw CustomError(ErrorStatus.USER_IS_NOT_FRIEND)
        return assembleRepo.save(assemble.let {
            it.copy(
                users = it.users + AssembleUser(
                    target,
                    message,
                    InviteStatus.PENDING
                )
            )
        })
    }

    fun replyAssemble(user: User, hostId: UUID, isAccepted: Boolean): Assemble {
        val info = getAssembleInfo(hostId)
        val n = info.users.find { it.user == user.id } ?: throw CustomError(ErrorStatus.NOT_ASSEMBLE_MEMBER)
        if (n.status != InviteStatus.PENDING)
            throw CustomError(ErrorStatus.ALREADY_ANSWERED)
        return assembleRepo.save(info.copy(users = (info.users - n) + n.copy(status = InviteStatus.ACCEPTED)))
    }
}