package controlador;

import com.sun.deploy.uitoolkit.impl.fx.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class util {
	public static byte[] obtenerByteTablaARP(String ipAComparar)
    {
        String aux=null;
        try {
            Process arpQuery =Runtime.getRuntime().exec("arp -a");     
          BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(arpQuery.getInputStream())); 
          
          while((aux=inStreamReader.readLine()) != null)
          {
              
              
             //System.out.print(aux);
             
              if(verSiIpCoincide(aux,ipAComparar))
              {
                  //Sacar mac
                  //System.out.print(" Ip es igual \n");
                  byte[]resultado=sacarMacEnByteTablaARP(aux);
                  
                  for(int i=0;i<6;i++)
                  {
                      System.out.print(resultado[i]& 0xff);
                      System.out.print("-");
                  }
                  System.out.print("\n");
                  return resultado; 
              }
              //System.out.print("\n");
          }
          
          
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
          return null;
    }
    
   public static boolean verSiIpCoincide(String tupla, String ipAComparar)
    {
          boolean aux=false;
          StringTokenizer tokens=new StringTokenizer(tupla, " ");
          if(tokens.countTokens()>0)
          {
            String auxiliar= tokens.nextToken();
            if(auxiliar.trim().equals(ipAComparar.trim()))
            {
                aux= true;
            }
          }
          return aux;
    }
    public static byte[] sacarMacEnByteTablaARP(String tupla)
    {
        StringTokenizer tokens=new StringTokenizer(tupla, " ");
        String auxiliar= tokens.nextToken();
        auxiliar= tokens.nextToken();
        
        tokens=new StringTokenizer(auxiliar, "-");
        ////////////////////////////////////////////
           byte[]resultado ={0,0,0,0,0,0};
           
           String numero=tokens.nextToken().trim(); 
           resultado[0]=(byte)(Integer.parseInt(numero,16)& 0xff);
           numero=tokens.nextToken().trim(); 
           resultado[1]=(byte)(Integer.parseInt(numero,16)& 0xff);      
           
           numero=tokens.nextToken().trim(); 
           resultado[2]=(byte)(Integer.parseInt(numero,16)& 0xff);
           
           numero=tokens.nextToken().trim(); 
           resultado[3]=(byte)(Integer.parseInt(numero,16)& 0xff);
           
           numero=tokens.nextToken().trim(); 
           resultado[4]=(byte)(Integer.parseInt(numero,16)& 0xff);
           
           numero=tokens.nextToken().trim(); 
           resultado[5]=(byte)(Integer.parseInt(numero,16)& 0xff);
        ///////////////////////////////////////////
        return resultado;
    }
    
}
