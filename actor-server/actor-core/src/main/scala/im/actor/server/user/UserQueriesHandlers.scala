package im.actor.server.user

import akka.actor.ActorSystem
import akka.http.scaladsl.util.FastFuture
import akka.pattern.pipe
import im.actor.api.rpc.users.ApiUser
import im.actor.server.ApiConversions._
import im.actor.server.acl.ACLUtils

private[user] trait UserQueriesHandlers {
  self: UserProcessor ⇒

  import UserQueries._

  protected def getAuthIds(state: UserState): Unit =
    sender() ! GetAuthIdsResponse(state.authIds)

  protected def getApiStruct(state: UserState, clientUserId: Int, clientAuthId: Long)(implicit system: ActorSystem): Unit = {
    (for {
      localName ← if (clientUserId == state.id || clientUserId == 0)
        FastFuture.successful(None)
      else
        userExt.getLocalName(clientUserId, state.id)
    } yield GetApiStructResponse(ApiUser(
      id = userId,
      accessHash = ACLUtils.userAccessHash(clientAuthId, userId, state.accessSalt),
      name = state.name,
      localName = UserUtils.normalizeLocalName(localName),
      sex = Some(state.sex),
      avatar = state.avatar,
      isBot = Some(state.isBot),
      contactInfo = UserUtils.defaultUserContactRecords(state.phones.toVector, state.emails.toVector, state.socialContacts.toVector),
      nick = state.nickname,
      about = state.about,
      external = state.external,
      preferredLanguages = state.preferredLanguages.toVector,
      timeZone = state.timeZone,
      botCommands = state.botCommands
    ))) pipeTo sender()
  }

  protected def getContactRecords(state: UserState): Unit =
    sender() ! GetContactRecordsResponse(state.phones, state.emails)

  protected def checkAccessHash(state: UserState, senderAuthId: Long, accessHash: Long): Unit =
    sender() ! CheckAccessHashResponse(isCorrect = accessHash == ACLUtils.userAccessHash(senderAuthId, userId, state.accessSalt))

  protected def getAccessHash(state: UserState, clientAuthId: Long): Unit =
    sender() ! GetAccessHashResponse(ACLUtils.userAccessHash(clientAuthId, userId, state.accessSalt))

  protected def getUser(state: UserState): Unit = sender() ! state

  protected def isAdmin(state: UserState): Unit = sender() ! IsAdminResponse(state.isAdmin.getOrElse(false))

  protected def getName(state: UserState): Unit = sender() ! GetNameResponse(state.name)
}
