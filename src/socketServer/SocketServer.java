package socketServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * this program reads from a stream socket and echos the data back to the client
 */
public class SocketServer {
	public static void main(String[] args) {
		final int   	BUFLEN = 512;
		final int   	OFFSET = 1;
		byte	    	bBuffer[] = new byte[BUFLEN];
		ServerSocket	serverSocket = null;
		Socket      	socket = null;
		InputStream 	inpt = null;
		OutputStream	outpt = null;
		final int   	port = 5000;
		int 	    	cbRead, cbWrite;

		System.out.println("Server started on port " + port);

		// wait for connection request and connect to client

		try {
			serverSocket = new ServerSocket(port);
			socket	= serverSocket.accept();
			System.out.println("connection: "+ serverSocket +" - "+ socket);
		} catch (UnknownHostException e) {
			System.out.println("Host not found: " + e.getMessage());
		} catch (IOException ioe) {
			System.out.println("I/O Error " + ioe.getMessage());
		}

		// create stream for reading and writing

		try {
			inpt	= socket.getInputStream();
			outpt	= socket.getOutputStream();
			System.out.println("streams: " + inpt +" : "+ outpt);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// read data and send it back
		bBuffer[0]	= '~';
		try {
			do {
				cbRead  	= inpt.read(bBuffer, OFFSET, BUFLEN-OFFSET);
				if(cbRead < 0) break;
				cbWrite 	= cbRead + OFFSET;
				String s	= new String(bBuffer, 0, cbWrite);
				System.out.println("read["+cbRead+"]<" + s + ">");
				outpt.write(bBuffer, 0, cbWrite);
				outpt.flush();
			} while (cbRead>1 || bBuffer[OFFSET] != (byte)'.');
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// close connection

		try {
			serverSocket.close();
			System.out.println("serverSocket closed");
		} catch (IOException e) {
			System.out.println("Close error: " + e.getMessage());
			e.printStackTrace();
		}

	}
}
