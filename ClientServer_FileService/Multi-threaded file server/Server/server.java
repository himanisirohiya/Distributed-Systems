import java.io.*;
import java.net.*;
import java.util.*;
// Server class
class Server {

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

	public static void main(String[] args)
	{
		ServerSocket ss = null;

		try {
			// server is listening on port 1234
			ss = new ServerSocket(888);
			ss.setReuseAddress(true);
            System.out.println("Waiting for client(s) to connect");
            int threadId = 0;
			// running infinite loop for getting
			// client request
			while (true) {
                //run continuously
                // connect it to client socket
                Socket sock = ss.accept();
                System.out.println("New client connection established");
                // create a new thread object
                //used to keep a count of threads created or clients interacting with server
                ++threadId;
                ClientHandler clients = new ClientHandler(sock,threadId);
                // used to handle each client separately
                new Thread(clients).start();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (ss != null) {
				try {
					ss.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ClientHandler class
	private static class ClientHandler implements Runnable {
		Socket s;
        int threadCount;

		// Constructor
		public ClientHandler(Socket socket, int threadId)
		{
			this.s = socket;
            this.threadCount = threadId;
		}

		public void run()
		{

			PrintWriter out = null;
			BufferedReader in = null;
			try {
            // to send data to the client
                PrintStream ps = new PrintStream(s.getOutputStream());

                // to read data coming from the client
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                // to read data from the keyboard
                BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
                //Scanner in = new Scanner(s.getInputStream());
                InputStream is = s.getInputStream();
                byte[] buf = new byte[1024*1024];
                String fileName, filePath;
                String p = System.getProperty("user.dir");
                // server executes continuously
                while (true) {
                    String str, str1, choice;
                    ps.println("Enter the operation to be performed:\t1.Upload\t2.Download\t3.Delete\t4.Rename");
                    choice = br.readLine();
                
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
					    System.out.println("invalid choice");
                    
		

                // close connection
                ps.close();
                br.close();
                kb.close();
                //ss.close();
                s.close();

                // terminate application
                //System.exit(0);

		 } // end of while
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
                System.out.println("Client "+threadCount+" exited");

			}
		}
	}
}
