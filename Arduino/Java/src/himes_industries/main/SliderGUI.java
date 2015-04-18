package himes_industries.main;

import himes_industries.util.Communication;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderGUI extends JFrame {
	private JSlider vSlider, hSlider;
	
	//This number is used to convert 0-180 range to 0-127 range. Catering to the limited 0-255 byte datatype range.
	private final double conversion = 1.41732283464567;
	
	public SliderGUI(String[] args) throws Exception {
		String port = args[0];
		Communication.connect(port); 
		
		Communication.sendInt(191);//Initialize pan motor
		Communication.sendInt(63);//Initialize tilt motor
		
		vSlider = new JSlider(JSlider.VERTICAL, 0, 180, 90);
		vSlider.setMajorTickSpacing(10);
		vSlider.setMinorTickSpacing(1);
		vSlider.setPaintTicks(true);
		vSlider.setPaintLabels(true);
		
		vSlider.addChangeListener(new ChangeListener()
		{
	            public void stateChanged( ChangeEvent e)
	            {
	            	//Sends numbers 128 to 255 so they're identified as tilt values
	            	int sliderToMotor = (int)Math.round(vSlider.getValue()/conversion)+128;
	            	Communication.sendInt(sliderToMotor);
	            	System.out.println(""+sliderToMotor);
	            }
		});
		
		hSlider = new JSlider(0, 180, 90);
		hSlider.setMajorTickSpacing(10);
		hSlider.setMinorTickSpacing(1);
		hSlider.setPaintTicks(true);
		hSlider.setPaintLabels(true);
		
		hSlider.addChangeListener(new ChangeListener()
		{
	            public void stateChanged( ChangeEvent e)
	            {
	            	//Sends numbers 0 to 127 so they're identified as pan values
	            	int sliderToMotor = (int)Math.round(hSlider.getValue()/conversion);
	            	Communication.sendInt(sliderToMotor);
	            	System.out.println(""+sliderToMotor);
	            }
		});
		
		
		
		Container canvas = getContentPane();
		canvas.setLayout(new BorderLayout());
		//Add components to canvas.
		canvas.add(vSlider, BorderLayout.WEST);
		canvas.add(hSlider, BorderLayout.SOUTH);
	}
}
