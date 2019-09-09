/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.github.ffalcinelli.jdivert.Enums;
import com.github.ffalcinelli.jdivert.Enums.Flag;
import com.github.ffalcinelli.jdivert.Enums.Layer;
import com.github.ffalcinelli.jdivert.Packet;
import com.github.ffalcinelli.jdivert.WinDivert;
import com.github.ffalcinelli.jdivert.exceptions.WinDivertException;
import com.github.ffalcinelli.jdivert.windivert.WinDivertDLL;
import com.sun.jna.platform.unix.X11;
import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author redes
 */
public class ProyectoRedes {

    jpcap.NetworkInterface[] equipos;
    public Thread run;
    public boolean capturar;

    public ProyectoRedes() {
        capturar = true;
        String filtro = "udp or icmp";
        try {
            filtro = leerTxt();
        } catch (IOException ex) {
            Logger.getLogger(ProyectoRedes.class.getName()).log(Level.SEVERE, null, ex);
        }

        filtrar(filtro);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        ProyectoRedes p = new ProyectoRedes();

    }

    private String leerTxt() throws FileNotFoundException, IOException {
        String filtro = "false";//Por defecto, deje pasar todo
        String linea, protocolo = null, bool = null, ruta = "ejemplo.txt";
        boolean b;
        int cont = 0;
        BufferedReader sc = new BufferedReader(new FileReader(ruta));
        linea = sc.readLine();

        StringTokenizer token = new StringTokenizer(linea, " ");
        cont++;
        bool = token.nextToken();
         System.out.println(bool);
        if (bool.equals("#icmp")) {
            bool = token.nextToken();
             System.out.println(filtro);
            if (bool.equals("true")) {
                linea = sc.readLine();
                ArrayList<String> typesABloquear = new ArrayList<>();
             
                 System.out.println(linea);
                while(!linea.equals("#icmpFin")){
                    filtro="";
                    typesABloquear.add(linea);
                    linea = sc.readLine();
                }
                
                for(int i = 0; i < typesABloquear.size(); i++){
                    if(typesABloquear.size()==1){
                        filtro ="icmp.Type="+typesABloquear.get(i);
                    }else if((i+1)!=typesABloquear.size()){
                        filtro =filtro+"icmp.Type="+typesABloquear.get(i)+" or ";
                    }else{
                         filtro =filtro+"icmp.Type="+typesABloquear.get(i);
                    }
                    
                }
            } else {
                filtro = "icmp";
            }
        }
        linea = sc.readLine();
        token = new StringTokenizer(linea, " ");
        bool = token.nextToken();
        if(bool.equals("#ip")){
            bool = token.nextToken();
            if(bool.equals("true")){
                linea = sc.readLine();
                ArrayList<String> ipS = new ArrayList<>();
                ArrayList<String> reglas = new ArrayList<>();
                if(!linea.equals("#ipFin")){
                    if(filtro.equals("false")){
                        filtro = "";
                    }else{
                        filtro= filtro + " or ";
                    }
                }
                while(!linea.equals("#ipFin")){
                    
                    token = new StringTokenizer(linea, " ");
                    bool = token.nextToken();
                    ipS.add(bool);
                    
                    bool = token.nextToken();
                    reglas.add(bool);
                    
                    
                    linea = sc.readLine();
                }
                for(int i = 0; i < ipS.size();i++){
                    if(ipS.size()==1){
                        if(reglas.get(i).equals("destino")){
                            filtro =filtro + "ip.DstAddr="+ipS.get(i);
                        }else{
                            filtro =filtro + "ip.SrcAddr="+ipS.get(i);
                        }
                        
                    }else if((i+1)!=ipS.size()){
                        if(reglas.get(i).equals("destino")){
                            filtro =filtro + "ip.DstAddr="+ipS.get(i)+" or ";
                        }else{
                            filtro =filtro + "ip.SrcAddr="+ipS.get(i)+" or ";
                        }
                    }else{
                          if(reglas.get(i).equals("destino")){
                            filtro =filtro + "ip.DstAddr="+ipS.get(i);
                        }else{
                            filtro =filtro + "ip.SrcAddr="+ipS.get(i);
                        }
                    }
                }
            }else{
                if(filtro.equals("false")){
                    filtro = "ip";
                }else{
                    filtro = filtro + " or ip";
                }
            }
        }
        linea = sc.readLine();
        token = new StringTokenizer(linea, " ");
        bool = token.nextToken();
        if(bool.equals("#udp")){
            bool = token.nextToken();
            if(bool.equals("true")){
                linea = sc.readLine();
                ArrayList<String> ipS = new ArrayList<>();
                ArrayList<String> reglas = new ArrayList<>();
                if(!linea.equals("#udpFin")){
                    if(filtro.equals("false")){
                        filtro = "";
                    }else{
                        filtro= filtro + " or ";
                    }
                }
                while(!linea.equals("#udpFin")){
                    
                    token = new StringTokenizer(linea, " ");
                    bool = token.nextToken();
                    ipS.add(bool);
                    
                    bool = token.nextToken();
                    reglas.add(bool);
                    
                    
                    linea = sc.readLine();
                }
                for(int i = 0; i < ipS.size();i++){
                    if(ipS.size()==1){
                        if(reglas.get(i).equals("destino")){
                            filtro =filtro + "udp.DstPort="+ipS.get(i);
                        }else{
                            filtro =filtro + "udp.SrcPort="+ipS.get(i);
                        }
                        
                    }else if((i+1)!=ipS.size()){
                        if(reglas.get(i).equals("destino")){
                            filtro =filtro + "udp.DstPort="+ipS.get(i)+" or ";
                        }else{
                            filtro =filtro + "udp.SrcPort="+ipS.get(i)+" or ";
                        }
                    }else{
                          if(reglas.get(i).equals("destino")){
                            filtro =filtro + "udp.DstPort="+ipS.get(i);
                        }else{
                            filtro =filtro + "udp.SrcPort="+ipS.get(i);
                        }
                    }
                }
            }else{
                if(filtro.equals("false")){
                    filtro = "udp";
                }else{
                    filtro = filtro + " or udp";
                }
            }
            
        
        
        }
        
        linea = sc.readLine();
        token = new StringTokenizer(linea, " ");
        bool = token.nextToken();
        if(bool.equals("#tcp")){
            bool = token.nextToken();
            if(bool.equals("true")){
                linea = sc.readLine();
                ArrayList<String> ipS = new ArrayList<>();
                ArrayList<String> reglas = new ArrayList<>();
                if(!linea.equals("#tcpFin")){
                    if(filtro.equals("false")){
                        filtro = "";
                    }else{
                        filtro= filtro + " or ";
                    }
                }
                while(!linea.equals("#tcpFin")){
                    
                    token = new StringTokenizer(linea, " ");
                    bool = token.nextToken();
                    ipS.add(bool);
                    
                    bool = token.nextToken();
                    reglas.add(bool);
                    
                    
                    linea = sc.readLine();
                }
                for(int i = 0; i < ipS.size();i++){
                    if(ipS.size()==1){
                        if(reglas.get(i).equals("destino")){
                            filtro =filtro + "tcp.DstPort="+ipS.get(i);
                        }else{
                            filtro =filtro + "tcp.SrcPort="+ipS.get(i);
                        }
                        
                    }else if((i+1)!=ipS.size()){
                        if(reglas.get(i).equals("destino")){
                            filtro =filtro + "tcp.DstPort="+ipS.get(i)+" or ";
                        }else{
                            filtro =filtro + "tcp.SrcPort="+ipS.get(i)+" or ";
                        }
                    }else{
                          if(reglas.get(i).equals("destino")){
                            filtro =filtro + "tcp.DstPort="+ipS.get(i);
                        }else{
                            filtro =filtro + "tcp.SrcPort="+ipS.get(i);
                        }
                    }
                }
            }else{
                if(filtro.equals("false")){
                    filtro = "tcp";
                }else{
                    filtro = filtro + " or tcp";
                }
            }
            
        
        
        }
        
         linea = sc.readLine();
        token = new StringTokenizer(linea, " ");
        bool = token.nextToken();
        if(bool.equals("#http")){
              bool = token.nextToken();
            if(bool.equals("true")){
                linea = sc.readLine();
                if(!linea.equals("#httpFin")){
                    if(filtro.equals("false")){
                        filtro = "";
                    }else{
                        filtro= filtro + " or ";
                    }
                }    
            }else{
                if(filtro.equals("false")){
                    filtro = "tcp.DstPort=80 or tcp.SrcPort=80";
                }else{
                    filtro = filtro + " or tcp.DstPort=80 or tcp.SrcPort=80";
                }
            }
            
        }
        
        System.out.println(filtro);

        sc.close();
        return filtro;
    }

    private void filtrar(String filtro) {
        WinDivert w = new WinDivert(filtro, Enums.Layer.NETWORK_FORWARD, 0, Enums.Flag.DROP);

        try {
            w.open(); // packets will be captured from now on
        } catch (WinDivertException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ProyectoRedes.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            Packet packet = null;
            try {
                packet = w.recv();

// read a single packet
            } catch (WinDivertException ex) {
                Logger.getLogger(ProyectoRedes.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(packet.getDstAddr());
            if (true) {
                try {

                    w.send(packet);
                  // re-inject the packet into the network stack

                    //w.close();  // stop capturing packets
                } catch (WinDivertException ex) {
                    System.out.println(ex.toString());
                    Logger.getLogger(ProyectoRedes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
}
