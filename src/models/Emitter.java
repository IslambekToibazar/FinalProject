package models;
import java.util.LinkedList;

public abstract class Emitter {
	public abstract LinkedList<Particle> emit(double x, double y);
}

