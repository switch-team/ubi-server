package dev.jombi.ubi.service

import dev.jombi.ubi.entity.Assemble
import dev.jombi.ubi.entity.AssembleUser
import dev.jombi.ubi.entity.User
import dev.jombi.ubi.repository.mongo.AssembleRepository
import dev.jombi.ubi.util.response.CustomError
import dev.jombi.ubi.util.response.ErrorStatus
import dev.jombi.ubi.util.state.InviteStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class AssembleService(
    private val assembleRepo: AssembleRepository,
    private val friend: FriendService,
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
    // user 나 hostid 방장
    fun replyAssemble(user: User, hostId: UUID, isAccepted: Boolean): Assemble {
        val info = getAssembleInfo(hostId)
        val n = info.users.find { it.user == user.id } ?: throw CustomError(ErrorStatus.NOT_ASSEMBLE_MEMBER)
        if (n.status != InviteStatus.PENDING)
            throw CustomError(ErrorStatus.ALREADY_ANSWERED)
        val state = if (isAccepted) InviteStatus.ACCEPTED else InviteStatus.REJECTED
        return assembleRepo.save(info.copy(users = (info.users - n) + n.copy(status = state)))
    }

    // 친구가 연 assemble 찾기
    fun getRelatedJoin(user: User): List<Assemble> {
        val friendsId = friend.getFriendList(user).map { it.id }
        return friendsId.mapNotNull { assembleRepo.queryAssembleByHostIs(it) }
    }

    // user가 host(친구)한테 참가요청
    fun requestJoin(user: User, target: UUID, message: String): Assemble {
        val join = getAssembleInfo(target)
        if (join.host == user.id || join.users.any { it.user == user.id})
            throw CustomError(ErrorStatus.USER_ALREADY_JOINED_ASSEMBLE)
        if (!friend.isUserFriend(user, target))
            throw CustomError(ErrorStatus.USER_IS_NOT_FRIEND)
        return assembleRepo.save(join.let {
            it.copy(
                users = it.users + AssembleUser( // + = add
                    user.id,
                    message,
                    InviteStatus.REVERSEPENDING
                )
            )
        })
    }
    // host가 reply 함
    // user가 host, target이 join보낸 사람
    fun replyJoin(user: User, target: UUID , isAccepted: Boolean): Assemble {
        val info = getAssembleInfo(user.id)
        val n = info.users.find { it.user == target } ?: throw CustomError(ErrorStatus.NOT_ASSEMBLE_MEMBER)
        if (n.status != InviteStatus.REVERSEPENDING)
            throw CustomError(ErrorStatus.ALREADY_ANSWERED)
        val state = if (isAccepted) InviteStatus.ACCEPTED else InviteStatus.REJECTED
        return assembleRepo.save(info.copy(users = (info.users - n) + n.copy(status = state)))
    }
}