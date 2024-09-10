package onlineshop.exception

sealed trait AError extends Throwable {
  def cause: String
  def errorCode: String
  override def getMessage: String = cause
}

object AError {
  final case class Internal(cause: String) extends AError {
    override def errorCode: String = "INTERNAL"
  }
  final case class NotAllowed(cause: String) extends AError {
    override def errorCode: String = "NOT_ALLOWED"
  }
  final case class BadRequest(cause: String) extends AError {
    override def errorCode: String = "BAD_REQUEST"
  }
  sealed trait AuthError extends AError
  object AuthError {
    final case class NoSuchUser(cause: String) extends AuthError {
      override def errorCode: String = "NOT_FOUND"
    }
    final case class InvalidToken(cause: String) extends AuthError {
      override def errorCode: String = "INVALID_TOKEN"
    }
    final case class PasswordDoesNotMatch(cause: String) extends AuthError {
      override def errorCode: String = "AUTHENTICATION"
    }
  }
}
