package models;
import java.util.LinkedList;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class FireEmitter extends Emitter{

	@Override
	public LinkedList<Particle> emit(double x, double y) {
		LinkedList<Particle> particles = new LinkedList<>();
		int numParticles = 1;
		for(int i =0; i<numParticles;i++){
			Particle p = new Particle(x,y,new Point2D((Math.random()-0.5)*0.65,0.2-Math.random()+0.1),
					GameBoardModel.UNSTOPPABLE_BALL_SIZE/2, 1, Color.rgb(215, 30, 65)
					);
			particles.add(p);
		}

		return particles;
	}

}
