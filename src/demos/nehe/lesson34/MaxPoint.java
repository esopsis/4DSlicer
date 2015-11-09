package demos.nehe.lesson34;

public class MaxPoint {
	double myXCo;
	double myYCo;
	double myZCo;
	double myHeightFactor;
	double xStart;
	double yStart;
	double zStart;
	double myXYAngle;
	double myXZAngle;
	double myYZAngle;
	double myXYRadius;
	double myXZRadius;
	double myYZRadius;
	public MaxPoint(double xCo, double yCo, double zCo, double heightFactor) {
		myXCo = xStart = xCo;
		myYCo = yStart = yCo;
		myZCo = zStart = zCo;
		myHeightFactor = heightFactor;
		myXYAngle = 0;
		myXZAngle = 0;
		myYZAngle = 0;
		myXYRadius = 0;
		myXZRadius = 0;
		myYZRadius = 0;
	}
	public double getX() {
		return myXCo;
	}
	public double getY() {
		return myYCo;
	}
	public double getZ() {
		return myZCo;
	}
	public double getHeightFactor() {
		return myHeightFactor;
	}
	public double getXStart() {
		return xStart;
	}
	public double getYStart() {
		return yStart;
	}
	public double getZStart() {
		return zStart;
	}
	public void setX(double value) {
		myXCo = value;
	}
	public void setY(double value) {
		myYCo = value;
	}
	public void setZ(double value) {
		myZCo = value;
	}
	public void incX(double value) {
		myXCo += value * Renderer.speed;
	}
	public void incY(double value) {
		myYCo += value * Renderer.speed;
	}
	public void incZ(double value) {
		myZCo += value * Renderer.speed;
	}
	public void decrX(double value) {
		myXCo -= value * Renderer.speed;
	}
	public void decrY(double value) {
		myYCo -= value * Renderer.speed;
	}
	public void decrZ(double value) {
		myZCo -= value * Renderer.speed;
	}
	public void setXYAngle(double angle) {
		myXYAngle = angle;
	}
	public double getXYAngle() {
		return myXYAngle;
	}
	public void setXYRadius(double radius) {
		myXYRadius = radius;
	}
	public double getXYRadius() {
		return myXYRadius;
	}
	public void incXYAngle(double value) {
		myXYAngle += value * Renderer.speed;
	}
	public void decrXYAngle(double value) {
		myXYAngle -= value * Renderer.speed;
	}
	public void setXZAngle(double angle) {
		myXZAngle = angle;
	}
	public double getXZAngle() {
		return myXZAngle;
	}
	public void setXZRadius(double radius) {
		myXZRadius = radius;
	}
	public double getXZRadius() {
		return myXZRadius;
	}
	public void incXZAngle(double value) {
		myXZAngle += value * Renderer.speed;
	}
	public void decrXZAngle(double value) {
		myXZAngle -= value * Renderer.speed;
	}
	public void setYZAngle(double angle) {
		myYZAngle = angle;
	}
	public double getYZAngle() {
		return myYZAngle;
	}
	public void setYZRadius(double radius) {
		myYZRadius = radius;
	}
	public double getYZRadius() {
		return myYZRadius;
	}
	public void incYZAngle(double value) {
		myYZAngle += value * Renderer.speed;
	}
	public void decrYZAngle(double value) {
		myYZAngle -= value * Renderer.speed;
	}
}