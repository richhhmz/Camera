package himes_industries.util;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;

import himes_industries.util.Global;

public class Communication {

	private static int TIMEOUT = 2000;
	private static OutputStream portOut;
	private static CommPort commPort = null;
	
	public static void connect ( String port ) throws Exception
    {	
		
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);        
        if ( portIdentifier.isCurrentlyOwned() )
        {
            Global.handleError("Connecting: port "+port+" is already in use");            
            return;
        }
        else
        {   
        	//Was getting PortInUseException on Mac version, solution was to create a folder /var/lock
            commPort = portIdentifier.open(Communication.class.getName(), TIMEOUT);            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(Global.BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                serialPort.addEventListener(new Reader(serialPort.getInputStream()));
                serialPort.notifyOnDataAvailable(true);
                portOut = serialPort.getOutputStream();
            }
            else
            {
            	Global.handleError("Connecting: " + port + " is not a serial port");
            	return;
            }
        }     
    }

	public static void closeCommPort(){		
		commPort.close();
	}
	
	public static void sendLine(String out){
		out = out.replaceAll(Global.NEWLINE, "");
		Communication.sendBytes(out.getBytes());
		Communication.sendByte((byte)'\n');
		Message.sent(out);
	}
	
	public static void sendBytes(byte[] out){
		for(int i=0;i<out.length;i++){
			Communication.sendByte(out[i]);
		}
	}
	
    public static void sendByte(byte b){
    	try{
    		portOut.write(b);
    	}
    	catch(Exception ex){
    		Global.handleException(ex);
    	}
    }

    public static void sendInt(int c){
    	sendByte((byte)c);
    }
    
    public static class Reader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public Reader ( InputStream in )
        {
            this.in = in;
        }
        
        @Override
        /**
         * For more, see http://en.wikibooks.org/wiki/Serial_Programming/Serial_Java
         */
        public void serialEvent(SerialPortEvent event) {

        	switch(event.getEventType()) {
        	
                case SerialPortEvent.DATA_AVAILABLE:
                    dataAvailable(event);
                    break;

				case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
					outputBufferEmpty(event);
					break;

                case SerialPortEvent.BI:
                    breakInterrupt(event);
                    break;

                case SerialPortEvent.CD:
                    carrierDetect(event);
                    break;

                case SerialPortEvent.CTS:
                    clearToSend(event);
                    break;

                case SerialPortEvent.DSR:
                    dataSetReady(event);
                    break;

                case SerialPortEvent.FE:
                    framingError(event);
                    break;

                case SerialPortEvent.OE:
                    overrunError(event);
                    break;

                case SerialPortEvent.PE:
                    parityError(event);
                    break;
                    
                case SerialPortEvent.RI:
                    ringIndicator(event);
                    break;

            }
        }

    	protected void dataAvailable(SerialPortEvent event){
        	int data;
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                String message = new String(buffer,0,len);
                Message.received(message);
            }
            catch ( Exception e )
            {
                Global.handleException(e);
            }             
        }
        
    	protected void outputBufferEmpty(SerialPortEvent event){
    		Message.info("Output Buffer Empty");
    	}

    	protected void breakInterrupt(SerialPortEvent event){
    		Message.info("Break Interrupt");
    	}

    	protected void carrierDetect(SerialPortEvent event){
    		Message.info("Carrier Detect");
    	}

    	protected void clearToSend(SerialPortEvent event){
    		Message.info("Clear to Send");
    	}

    	protected void dataSetReady(SerialPortEvent event){
    		Message.info("Data Set Ready");
    	}

    	protected void framingError(SerialPortEvent event){
    		Message.info("Framing Error");
    	}

    	protected void overrunError(SerialPortEvent event){
    		Message.info("Overrun Error");
    	}

    	protected void parityError(SerialPortEvent event){
    		Message.info("Parity Error");
    	}

    	protected void ringIndicator(SerialPortEvent event){
    		Message.info("Ring Indicator");
    	}
    }

}
