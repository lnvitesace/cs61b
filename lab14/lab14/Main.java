package lab14;

import java.util.ArrayList;
import lab14lib.*;

public class Main {
	public static void main(String[] args) {
		Generator generator = new StrangeBitwiseGenerator(512);
		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
		gav.drawAndPlay(4096, 1000000);
	}
}