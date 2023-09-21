// Client2 class that
// sends data and receives also

import java.io.*;
import java.net.*;

class Client { 

	public static void main(String args[])
		throws Exception
	{
		// Create client socket
		Socket s = new Socket("localhost", 888);
		String fileName;
		// to send data to the server
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());

		// to read data coming from the server
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		// to read data from the keyboard
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

		PrintWriter pr = new PrintWriter(s.getOutputStream(), true);
		OutputStream os = s.getOutputStream();
		//send file contents
        // BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
		String str, str1, ch, msg;
		byte[] buf = new byte[1024*1024];
		// send to the server
		
		
		msg = br.readLine();
		System.out.println(msg);
		ch = kb.readLine();
		// send the choice of the client to server
		dos.writeBytes(ch + "\n");
		String p = System.getProperty("user.dir");
		
			if(ch.equals("1")){
				//upload the file on server side
				System.out.println("Enter the filename to be uploaded");
				fileName = kb.readLine();
				System.out.println(fileName);
				String filePath = p+"\\Files\\"+fileName;
				dos.writeBytes(fileName +"\n");
				FileInputStream fis = new FileInputStream(filePath);
				fis.read(buf, 0, buf.length);
				//send contents to server
				//dos.writeBytes(fileName);
				os.write(buf, 0, buf.length);
				fis.close();
				System.out.println(br.readLine());
			}
			else if(ch.equals("2")){
				// download the file from server
				System.out.println("Enter the file to download "+br.readLine());
				fileName = kb.readLine();
				dos.writeBytes(fileName +"\n");
				InputStream is = s.getInputStream();
				is.read(buf, 0, buf.length);
				String filePath1 = p+"\\Files\\"+fileName;
				FileOutputStream fos = new FileOutputStream(filePath1);
				fos.write(buf, 0, buf.length);
				fos.close();
				System.out.println(br.readLine());
			}
			else if(ch.equals("2")){
				// delete file from server side
				System.out.println("Enter the file to delete "+br.readLine());
				fileName = kb.readLine();
				dos.writeBytes(fileName +"\n");
				msg = br.readLine();
				System.out.println(msg);
				}
			else if(ch.equals("2")){
				// rename file stored on the server
				System.out.println("Enter the name of the file to be renamed");
				fileName = kb.readLine();
				dos.writeBytes(fileName+"\n");
				System.out.println("Enter the new filename");
				String rename = kb.readLine();
				dos.writeBytes(rename+"\n");
				msg = br.readLine();
				System.out.println(msg);
				}
			else
				System.out.println("Invalid choice");

		
		
		

		// close connection.
		dos.close();
		br.close();
		kb.close();
		s.close();
	}
}
