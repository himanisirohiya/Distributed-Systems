// Server2 class that
// receives data and sends data

import java.io.*;
import java.net.*;
import java.util.*;

class Server2 {

	public static void loadFolderContent(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                loadFolderContent(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
					result.add(f.getName());
                }
            }

        }
	}

	public static void main(String args[])
		throws Exception
	{

		// Create server Socket
		ServerSocket ss = new ServerSocket(888);
		System.out.println("Waiting for client(s) to connect");
		// connect it to client socket
		Socket s = ss.accept();
		System.out.println("Connection established");

		// to send data to the client
		PrintStream ps = new PrintStream(s.getOutputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		// to read data coming from the client
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		// to read data from the keyboard
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		//Scanner in = new Scanner(s.getInputStream());
		InputStream is = s.getInputStream();
		//FileOutputStream fos = new FileOutputStream(FileName);
		// String fileName = in.nextLine();
		// int fileSize = in.nextInt();
		//BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte[] buf = new byte[2048];
		String p = System.getProperty("user.dir");
		// server executes continuously
		String str, str1, choice, fileName, filePath;
		ps.println("Enter the operation to be performed:\t1.Upload\t2.Download\t3.Delete\t4.Rename");
		choice = br.readLine();
		while(true) {
			
				// str = "Enter the operation to be performed\n1.Upload\n2.Download\n3.Delete\n4.Re-upload";
				
				
				if(choice.equals("1")){
					//upload the file on server side
					//read data sent by client
					fileName = br.readLine();
					filePath = p+"\\Files\\"+fileName;
					is.read(buf, 0, buf.length);
					FileOutputStream fos = new FileOutputStream(filePath);
					fos.write(buf, 0, buf.length);
					fos.close();
					dos.writeBytes("File uploaded!!\n");
					
				}
				else if(choice.equals("2")){
					// send file to client to allow download of the file from server
					final File folder = new File(p+"\\Files\\");

					List<String> result = new ArrayList<>();

					loadFolderContent(".*", folder, result);
					String files = "";

					for (String si : result) {
						files+=" "+si;
					}
					dos.writeBytes(files+"\n");
					fileName = br.readLine();
					String filePath1 = p+"\\Files\\"+fileName;
					FileInputStream fis = new FileInputStream(filePath1);
					fis.read(buf, 0, buf.length);
					OutputStream os = s.getOutputStream();
					os.write(buf, 0, buf.length);
					fis.close();
					dos.writeBytes("File downloaded!!\n");
					
				}
				else if(choice.equals("3")){
					// delete file from server side
					final File folder = new File(p+"\\Files\\");

					List<String> result = new ArrayList<>();

					loadFolderContent(".*", folder, result);
					String files = "";

					for (String si : result) {
						files+=" "+si;
					}
					dos.writeBytes(files+"\n");
					fileName = br.readLine();
					File delFile = new File(p+"\\Files\\"+fileName);
					delFile.delete();
					dos.writeBytes("File deleted successfully!!\n");
					
				}
				else if(choice.equals("4")){
					// rename file stored on the server
					fileName = br.readLine();
					filePath = p+"/Files"+fileName;
					File file = new File(filePath);
					String newFile = br.readLine();
					String newpath = filePath+"/Files"+newFile;
					File renameFile = new File(newpath);
					file.renameTo(renameFile);
					dos.writeBytes("File renamed!!\n");
					
				}
				else
					break;

		
		

				
			
		}
			
		ps.close();
		br.close();
		kb.close();
		ss.close();
		s.close();
		
	}
	
}
