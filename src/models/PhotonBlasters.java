package models;

import java.util.LinkedList;

public class PhotonBlasters {
	private boolean isOn=false;
	private Rectangle rightBlaster;
	private Rectangle leftBlaster;
	private int bulletWidth = 6;
	private int bulletHeight = 17;
	private Coordinate batTopLeft;
	private double batWidth;
	private double width = 15;
	private double height = 21;

	protected PhotonBlasters(double batWidth, Coordinate batTopLeft) {
		this.batWidth=batWidth;
		this.batTopLeft=batTopLeft;
		double yPosition = batTopLeft.getY() - height;
		Coordinate leftBlasterTopLeft = new Coordinate(batTopLeft.getX(), yPosition);
		Coordinate rightBlasterTopLeft = new Coordinate(batTopLeft.getX() + batWidth - this.getWidth(), yPosition);
		leftBlaster = new Rectangle(leftBlasterTopLeft, width, height, RectangleType.PhotonBlaster);
		rightBlaster = new Rectangle(rightBlasterTopLeft, width, height, RectangleType.PhotonBlaster);
	}

	protected PhotonBlasters(double batWidth, Coordinate batTopLeft, int leftBlasterId, int rightBlasterId, boolean isOn) {
		this.batWidth=batWidth;
		this.batTopLeft=batTopLeft;
		this.isOn=isOn;
		double yPosition = batTopLeft.getY() - height;
		Coordinate leftBlasterTopLeft = new Coordinate(batTopLeft.getX(), yPosition);
		Coordinate rightBlasterTopLeft = new Coordinate(batTopLeft.getX() + batWidth - this.getWidth(), yPosition);
		leftBlaster = new Rectangle(leftBlasterTopLeft, width, height, leftBlasterId, RectangleType.PhotonBlaster);
		rightBlaster = new Rectangle(rightBlasterTopLeft, width, height, rightBlasterId, RectangleType.PhotonBlaster);
	}

	public PhotonBlasters getMove(double batWidth, Coordinate batTopLeft) {
		return new PhotonBlasters(batWidth, batTopLeft, this.getLeftBlaster().getId(), this.getRightBlaster().getId(),this.isOn);
	}
	public PhotonBlasters getOffScreen(){
		return new PhotonBlasters(0,new Coordinate(0,0), this.getLeftBlaster().getId(),this.getRightBlaster().getId(),isOn);
	}
	public PhotonBlasters setTurnedOn(boolean isOn){
		return new PhotonBlasters(batWidth, batTopLeft, this.getLeftBlaster().getId(), this.getRightBlaster().getId(),isOn);
	}

	public void fireRightBlaster(LinkedList<PhotonBullet> photonBullets){
		Coordinate middleOfBlaster = new Coordinate(rightBlaster.getTopLeftCoordinate().getX()+(rightBlaster.getWidth()/2)-(bulletWidth/2),
				rightBlaster.getTopLeftCoordinate().getY());
		photonBullets.add(new PhotonBullet(middleOfBlaster,bulletWidth,bulletHeight,RectangleType.PhotonBullet));
	}

	public void fireLeftBlaster(LinkedList<PhotonBullet> photonBullets) {
		Coordinate middleOfBlaster = new Coordinate(leftBlaster.getTopLeftCoordinate().getX()+(leftBlaster.getWidth()/2)-(bulletWidth/2),
				leftBlaster.getTopLeftCoordinate().getY());
		photonBullets.add(new PhotonBullet(middleOfBlaster, bulletWidth, bulletHeight, RectangleType.PhotonBullet));
	}

	public Rectangle getRightBlaster() {
		return rightBlaster;
	}

	public Rectangle getLeftBlaster() {
		return leftBlaster;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	public boolean isOn() {
		return isOn;
	}
}