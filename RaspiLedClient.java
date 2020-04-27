import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*; 

public class RaspiLedClient {

    public static void main(String[] args) throws Exception{
        //screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenW     = (int)screenSize.getWidth();
        int screenH     = (int)screenSize.getHeight();
        //
        int pixelSkip = 1;
        String hex;
        //variables for taking the screenshot
        int[] screenData;
        BufferedImage screenshot;
        Robot bot;
        Rectangle dispBounds = new Rectangle(new Dimension(screenW,screenH));

        InetAddress IPAddress = InetAddress.getByName(args[0]);
        DatagramSocket clientSocket = new DatagramSocket();

        try   {
      		bot = new Robot();
          while(true){
            screenshot = bot.createScreenCapture(dispBounds);
            screenData = ((DataBufferInt)screenshot.getRaster().getDataBuffer()).getData();
          	hex = getAvgScreenColor(screenW, screenH, pixelSkip, screenData);
            System.out.println("RGB = " + hex);
            clientSocket.send(new DatagramPacket(hex.getBytes(), 6, IPAddress, 1337));
          }
      	}
      	catch (AWTException e)  {
      		System.out.println("Robot class not supported by your system!");
      		System.exit(0);
      	}

        clientSocket.close();
    }

    private static String getAvgScreenColor(int screenW, int screenH, int pixelSkip, int[] screenData){
      int pixel;
      int r = 0;
      int g = 0;
      int b = 0;
      int[] col = new int[3];
      String hex;

      for(int i = 0; i < screenH; i += pixelSkip){
        for(int j = 0; j < screenW; j += pixelSkip){
          pixel = screenData[ i*screenW + j ];
          r += 0xff & (pixel>>16);
          g += 0xff & (pixel>>8 );
          b += 0xff &  pixel;
        }
      }

      col[0]  = r / (screenH/pixelSkip * screenW/pixelSkip);
      col[1]  = g / (screenH/pixelSkip * screenW/pixelSkip);
      col[2]  = b / (screenH/pixelSkip * screenW/pixelSkip);
      hex = String.format("%02x%02x%02x", col[0], col[1], col[2]);
      return hex;
    }

}
