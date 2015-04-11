package himes_industries.main;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JSlider;

public class SliderGUI extends JFrame {
	private JSlider slider;
	
	public SliderGUI() {
		slider = new JSlider(0, 180, 0);
		slider.setMajorTickSpacing(20);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		Container canvas = getContentPane();
		canvas.setLayout(new BorderLayout());
		//Add components to canvas.
		canvas.add(slider, BorderLayout.CENTER);
	}
}
