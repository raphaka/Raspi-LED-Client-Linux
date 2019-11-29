import java.awt.*;
import java.awt.image.*;

public class RaspiLedClient {

    public static void main(String[] args) {
        //screen size
        int screenW     = 3440;
        int screenH     = 1440;
        //
        int pixelSkip = 1;
        int[] avgColor;
        //variables for taking the screenshot
        int[] screenData;
        BufferedImage screenshot;
        Robot bot;
        Rectangle dispBounds = new Rectangle(new Dimension(screenW,screenH));

        try   {
      		bot = new Robot();
          while(true){
            screenshot = bot.createScreenCapture(dispBounds);
            screenData = ((DataBufferInt)screenshot.getRaster().getDataBuffer()).getData();
          	avgColor = getAvgScreenColor(screenW, screenH, pixelSkip, screenData);
            System.out.println("R = " + avgColor[0] + ";   G = "+ avgColor[1] + ";   B = " + avgColor[2] );
          }
      	}
      	catch (AWTException e)  {
      		System.out.println("Robot class not supported by your system!");
      		System.exit(0);
      	}


    }

    private static int[] getAvgScreenColor(int screenW, int screenH, int pixelSkip, int[] screenData){
      int pixel;
      int r = 0;
      int g = 0;
      int b = 0;
      int[] col = new int[3];

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
      return col;
    }

}
