package app.marlboroadvance.mpvar.ui.browser.networkstreaming.clients

import app.marlboroadvance.mpvar.domain.network.NetworkConnection
import app.marlboroadvance.mpvar.domain.network.NetworkProtocol

object NetworkClientFactory {
  fun createClient(connection: NetworkConnection): NetworkClient =
    when (connection.protocol) {
      NetworkProtocol.SMB -> SmbClient(connection)
      NetworkProtocol.FTP -> FtpClient(connection)
      NetworkProtocol.WEBDAV -> WebDavClient(connection)
    }
}
