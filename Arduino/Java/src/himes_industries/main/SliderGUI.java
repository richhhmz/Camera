package himes_industries.main;

import himes_industries.util.Communication;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderGUI extends JFrame {
	private JSlider slider;
	
	public SliderGUI(String[] args) throws Exception {
		String port = args[0];
		Communication.connect(port);
		
		slider = new JSlider(0, 180, 0);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		slider.addChangeListener(new ChangeListener()
		{
	            public void stateChanged( ChangeEvent e)
	            {
	            	Communication.sendInt(slider.getValue());
	            }
		});
		
		Container canvas = getContentPane();
		canvas.setLayout(new BorderLayout());
		//Add components to canvas.
		canvas.add(slider, BorderLayout.CENTER);
	}
}
