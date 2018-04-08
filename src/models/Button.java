package models;

public class Button extends Rectangle {
	private Runnable doesSomething;

	public Button(Runnable doesSomething) {
		super(doesSomething);
		this.doesSomething = doesSomething;
	}

	public Runnable getDoesSomething() {
		return doesSomething;
	}

}
