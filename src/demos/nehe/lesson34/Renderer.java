package demos.nehe.lesson34;

import demos.common.ResourceRetriever;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;

class Renderer implements GLEventListener {
	
	private double range = 7;
	//Randomizer variables
	private int pointNumber = 10;
	private double pointRange = (double) 5/7 * range;
	private int tankNumber = 3;
	private double tankRange = range / 2;
	//private MaxPoint[] pointArray = new MaxPoint[pointNumber];
	private Tank[] tankArray = new Tank [tankNumber];
	
	//Other variables
	byte[] slice;
	private float numColors = 4;
	private double xLocation = 0;
	private float colorFactor = 10;
	private double incrementFactor = 3;
	private float triSize = 2;
	static double speed = 1.0/80.0;
	
	
	private float mapYLoc = 0f;
	private float mapZLoc = .5f;
	private float mapXLoc = -.5f;
	private float mapScale = .4f;
	private float normalH = 3;
	/*
	private float mapYLoc = 0f;
	private float mapZLoc = 0f;
	private float mapSize = .5f;
	*/
	private boolean areTri = false;
	private float sliceXYAn = 0.0f;
	private float sliceXZAn = 0.0f;
	private float sliceYZAn = 0.0f;
	private float sliceX = 0.0f;
	private float sliceY = 0.0f;
	private float sliceZ = 0.0f;
	private double rotSpeed = 40;
	private float boxWidth = (float) 1.5;
	private float boxHeight = (float) 11;
	private double pixelToCo = (double) range / MAP_SIZE;
	private MaxPoint pt1 = new MaxPoint(1.5, -1, 1, 2);
	private MaxPoint pt2 = new MaxPoint(1.5, 1.5, .5, 0);
	private MaxPoint pt3 = new MaxPoint(-1.5, -.5, 1.5, 1);
	private Tank tank1 = new Tank(1.5, 1.0, .5);
	private MaxPoint[] pointArray = {pt1, pt2, pt3};
	//private MaxPoint[] tankArray = {tank1};
	/*
	private MaxPoint mapCr1 = new MaxPoint(-rangeInc / 2, -rangeInc / 2, 0, 0);
	private MaxPoint mapCr2 = new MaxPoint(-rangeInc / 2, rangeInc / 2, 0, 0);
	private MaxPoint mapCr3 = new MaxPoint(rangeInc / 2, rangeInc / 2, 0, 0);
	private MaxPoint mapCr4 = new MaxPoint(rangeInc / 2, -rangeInc / 2, 0, 0);
	*/
	private MaxPoint mapCr1 = new MaxPoint(-MAP_SIZE / 2, -MAP_SIZE / 2, 0, 0);
	private MaxPoint mapCr2 = new MaxPoint(-MAP_SIZE / 2, MAP_SIZE / 2, 0, 0);
	private MaxPoint mapCr3 = new MaxPoint(MAP_SIZE / 2, MAP_SIZE / 2, 0, 0);
	private MaxPoint mapCr4 = new MaxPoint(MAP_SIZE / 2, -MAP_SIZE / 2, 0, 0);
	private MaxPoint[] mapCrArr = {mapCr1, mapCr2, mapCr3, mapCr4};
	
    //private static final int MAP_SIZE = 1024;
	private static final int MAP_SIZE = 200;
	// Size Of Our .RAW Height Map (NEW)
    private static final int STEP_SIZE = 3;                        // Width And Height Of Each Quad (NEW)
    private byte[] heightMap = new byte[MAP_SIZE * MAP_SIZE]; // Holds The Height Map Data (NEW)
    private float scaleValue = .65f;                        // Scale Value For The Terrain (NEW)
    private boolean zoomIn;
    private boolean zoomOut;
    private boolean isLeft;
    private boolean isRight;
    private boolean isForward;
    private boolean isBackward;
    private boolean isVin;
    private boolean isVout;
    private boolean isYawL;
    private boolean isYawR;
    private boolean isPitchU;
    private boolean isPitchD;
    private boolean isRollL;
    private boolean isRollR;

    private float HEIGHT_RATIO = 1.0f;                        // Ratio That The Y Is Scaled According To The X And Z (NEW)
    private RenderMode renderMode = RenderMode.QUADS;                        // Polygon Flag Set To TRUE By Default (NEW)

    private GLU glu = new GLU();

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    public void zoomOut(boolean zoom) {
        zoomOut = zoom;
    }

    public void zoomIn(boolean zoom) {
        zoomIn = zoom;
    }
    
    public void moveLeft(boolean xStrafe) {
    	isLeft = xStrafe;
    }
    public void moveRight(boolean xStrafe){
    	isRight = xStrafe;
    }
    public void moveForwards(boolean yStrafe) {
    	isForward = yStrafe;
    }
    public void moveBackwards(boolean yStrafe){
    	isBackward = yStrafe;
    }
    public void moveVin(boolean thetaStrafe) {
    	isVin = thetaStrafe;
    }
    public void moveVout(boolean thetaStrafe){
    	isVout = thetaStrafe;
    }
    public void yawLeft(boolean yaw) {
    	isYawL = yaw;
    }
    public void yawRight(boolean yaw){
    	isYawR = yaw;
    }
    public void pitchUp(boolean pitch) {
    	isPitchU = pitch;
    }
    public void pitchDown(boolean pitch){
    	isPitchD = pitch;
    }
    public void rollLeft(boolean roll) {
    	isRollL = roll;
    }
    public void rollRight(boolean roll){
    	isRollR = roll;
    }

    public RenderMode getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(RenderMode renderMode) {
        if (renderMode == null)
            throw new IllegalArgumentException();

        this.renderMode = renderMode;
    }

    private void loadRawFile(String strName, byte[] pHeightMap) throws IOException {
        InputStream input = ResourceRetriever.getResourceAsStream(strName);
        readBuffer(input, pHeightMap);
        input.close();

        for (int i = 0; i < pHeightMap.length; i++)
            pHeightMap[i] &= 0xFF;                 //Quick fix
    }

    private static void readBuffer(InputStream in, byte[] buffer) throws IOException {
        int bytesRead = 0;
        int bytesToRead = buffer.length;
        while (bytesToRead > 0) {
            int read = in.read(buffer, bytesRead, bytesToRead);
            bytesRead += read;
            bytesToRead -= read;
        }
    }

    public void init(GLAutoDrawable drawable) {
    	
    	//Randomizer
		/*
		for (int i = 0; i < pointArray.length; i++) {
			//pointArray[i] = pt1;
			pointArray[i] = new MaxPoint(Math.random() * pointRange - pointRange / 2, Math.random() * pointRange - pointRange / 2, Math.random() * pointRange - pointRange / 2, 0);
		}
		*/
		for (int i = 0; i < tankArray.length; i++) {
			//pointArray[i] = pt1;
			tankArray[i] = new Tank(Math.random() * tankRange - tankRange / 2, Math.random() * tankRange - tankRange / 2, Math.random() * tankRange - tankRange / 2);
		}
		
		
        GL gl = drawable.getGL();

        gl.glShadeModel(GL.GL_SMOOTH);                                      // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.498f, 1.0f, 0.5f);                            // Asure Background
        gl.glClearDepth(1.0f);                                              // Depth Buffer Setup
        gl.glEnable(GL.GL_DEPTH_TEST);                                      // Enables Depth Testing
        gl.glDepthFunc(GL.GL_LEQUAL);                                       // The Type Of Depth Testing To Do
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);          // Really Nice Perspective Calculations
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        try {
            loadRawFile("demos/data/models/terrain.raw", heightMap);  // (NEW)
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void renderHeightMap(GL gl, byte[] pHeightMap) {              // Renders everything
    	//drawPyramid(0,0,0,gl);
    	//drawPyramid(3f, 3f, 3f, gl);
    	
        //gl.glTranslatef(1.5f, 0.0f, -6.0f);
    	//gl.glPushMatrix();
    	//gl.glLoadIdentity();
    	gl.glLoadIdentity();                                 // Reset The Matrix
        //              Position         View      Up Vector
    	glu.gluLookAt(MAP_SIZE / STEP_SIZE, MAP_SIZE / 4, MAP_SIZE * 1.25, MAP_SIZE / STEP_SIZE, 40 , 0, 0, 1, 0);// This Determines Where The Camera's Position And View Is
        //glu.gluLookAt(212, 60, 194, 186, 55, 171, 0, 1, 0);// This Determines Where The Camera's Position And View Is
        gl.glScalef(scaleValue, scaleValue * HEIGHT_RATIO, scaleValue);
    	gl.glTranslatef(MAP_SIZE / 2, 0, MAP_SIZE / 2);
    	//Draws pyramids on curve
    	if(areTri){
    		for (int i = 0; i < pointArray.length; i++) {
    			gl.glColor3f(1.0f, 0.0f, 0.0f);
    			drawPyramid((float) pointArray[i].getY(), (float) pointArray[i].getZ(), (float) pointArray[i].getX(), triSize, gl);
    		}
    	}
    	//System.out.println(pointArray[0].getZ());
    	
    	//Finds distance from tank to slice
    	//float tankZ = 0;
    	for(int i = 0; i < tankArray.length; i++) {
    		tankArray[i].setTankHeight(0);
    		for(int k = 0; k < pointArray.length; k++) {
    			tankArray[i].setTankHeight( tankArray[i].getTankHeight() + ((normalH * 1/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((0 - (pointArray[k].getXStart() - tankArray[i].getXStart())), 2) + Math.pow(((pointArray[k].getYStart() - tankArray[i].getYStart())- 0), 2) + Math.pow((0 - (pointArray[k].getZStart() - tankArray[i].getZStart())), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2)))));
    			//tankZ +=  ((normalH * (1/pixelToCo)/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((0 - (pointArray[k].getXStart() - tankArray[0].getXStart())), 2) + Math.pow(((pointArray[k].getYStart() - tankArray[0].getYStart())- 0), 2) + Math.pow((0 - (pointArray[k].getZStart() - tankArray[0].getZStart())), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2))));
    			//tankZ +=  ((normalH * (1)/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((0 - pointArray[k].getX()), 2) + Math.pow((pointArray[k].getY()-0), 2) + Math.pow((0 - pointArray[k].getZ()), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2))));
    			//boxZ +=  ((100/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((MAP_SIZE / 2 - pointArray[k].getX()), 2) + Math.pow((pointArray[k].getY()- MAP_SIZE / 2), 2) + Math.pow((MAP_SIZE / 2 - pointArray[k].getZ()), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2))));
    		}
    	}
    	
    	
    	//Draws tanks
    	for (int i = 0; i < tankArray.length; i++) {
    		//Sets tank color
    		double tankDist = (tankArray[i].getZ() + range / 2) / range;
    		if (tankDist > (double) (numColors - 1)/numColors) {
    			gl.glColor3f(1.0f, numColors * (1.0f - (float) tankDist), 0.0f);
    		}
    		else if (tankDist > (double) (numColors - 2)/numColors) {
    			//gl.glColor3f(5.0f * ((float) tankDist - (float) 1/5), 1.0f, 0.0f);
    			gl.glColor3f(numColors * ((float) tankDist - (float) (numColors - 1)/numColors) + 1f, 1.0f, 0.0f);
    		}
    		else if (tankDist > (double) (numColors - 3)/numColors) {
    			gl.glColor3f(0.0f, 1.0f, numColors * (1.0f - (float) tankDist) - 2.0f);
    		}
    		//else if (tankDist > (double) (numColors - 4)/numColors) {
    		else {
    			gl.glColor3f(0.0f, numColors * ((float) tankDist - (float) (numColors - 1)/numColors) + 3f, 1.0f);
    		}
    		/*
    		if (((tankArray[i].getZ() + range / 2) / range) < .5) {
    			gl.glColor3f((float) (-(tankArray[i].getZ()) / (range / 2)), 0.0f, 0.0f);
    		}
    		else {
    			gl.glColor3f(0.0f, 0.0f, (float) ((tankArray[i].getZ()) / (range / 2)));
    		}
    		*/
    		//Draws tank
			drawPyramid((float) tankArray[i].getY(), (float) tankArray[i].getTankHeight(), (float) tankArray[i].getX(), triSize, gl);
			//System.out.println(5.0f * (1.0f - (float) tankDist));
			//System.out.println(5.0f * (1.0f - (float) tankDist));
		}
    	//Draws lines to tanks
    	gl.glBegin(GL.GL_LINES);
    	gl.glColor3f(0.0f, 0f, 0f);
    	for(int i = 0; i < tankArray.length; i++) {
    		gl.glVertex3f((float) (tankArray[i].getX() / pixelToCo), 0f, (float) (tankArray[i].getY() / pixelToCo));
    		gl.glVertex3f((float) (tankArray[i].getX() / pixelToCo), (float) (tankArray[i].getTankHeight() / pixelToCo), (float) (tankArray[i].getY() / pixelToCo));
    	}
    	gl.glEnd();
    	//System.out.println(Math.pow((0 - (pointArray[0].getXStart() - tankArray[0].getXStart())), 2) + Math.pow(((pointArray[0].getYStart() - tankArray[0].getYStart())- 0), 2) + Math.pow((0 - (pointArray[0].getZStart() - tankArray[0].getZStart())),2)+ Math.pow((pointArray[0].getHeightFactor()/ 2),2));
		//System.out.println(Math.pow((0 - pointArray[0].getX()), 2) + Math.pow((pointArray[0].getY()-0), 2) + Math.pow((0 - pointArray[0].getZ()), 2) + Math.pow((pointArray[0].getHeightFactor()/ 2),2));
    	
    	gl.glTranslatef(MAP_SIZE * mapXLoc, MAP_SIZE * mapZLoc, MAP_SIZE * mapYLoc);
    	gl.glScaled(mapScale, mapScale, mapScale);
    	//draws map box
    	gl.glBegin(GL.GL_LINES);
    	gl.glColor3f(1.0f, 0f, 0f);
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, -MAP_SIZE / 2, -MAP_SIZE / 2);
    	
    	gl.glVertex3f(MAP_SIZE / 2, MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, -MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, MAP_SIZE / 2);
    	
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, -MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, -MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, -MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(MAP_SIZE / 2, -MAP_SIZE / 2, MAP_SIZE / 2);
    	
    	gl.glEnd();
    	//Draws map background
    	/*
    	gl.glBegin(GL.GL_QUADS);
    	gl.glColor3f(0f, 1f, 0f);
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, -MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, MAP_SIZE / 2);
    	gl.glVertex3f(-MAP_SIZE / 2, MAP_SIZE / 2, -MAP_SIZE / 2);
    	gl.glEnd();
    	*/
    	//Draws lines to map pyramids
    	
    	gl.glBegin(GL.GL_LINES);
    	gl.glColor3f(0f, 0f, 0f);
    	for (int i = 0; i < pointArray.length; i++) {
    		gl.glVertex3f((float) (pointArray[i].getXStart() / pixelToCo), (float) (pointArray[i].getZStart() / pixelToCo), (float) (pointArray[i].getYStart() /pixelToCo));
    		gl.glVertex3f((float) (pointArray[i].getXStart() / pixelToCo), MAP_SIZE / 2, (float) (pointArray[i].getYStart() / pixelToCo));
    	}
    	for (int i = 0; i < tankArray.length; i++) {
    		gl.glVertex3f((float) (tankArray[i].getXStart() / pixelToCo), (float) (tankArray[i].getZStart() / pixelToCo), (float) (tankArray[i].getYStart() /pixelToCo));
    		gl.glVertex3f((float) (tankArray[i].getXStart() / pixelToCo), MAP_SIZE / 2, (float) (tankArray[i].getYStart() / pixelToCo));
    	}
    	gl.glEnd();
    	//Draws map pyramids
    	gl.glColor3f(1.0f, .65f, 0.0f);
    	for (int i = 0; i < pointArray.length; i++) {
    		drawPyramid((float) pointArray[i].getYStart(), (float) pointArray[i].getZStart(), (float) pointArray[i].getXStart(), triSize, gl);
    	}
    	gl.glColor3f(1.0f, 0.0f, 1.0f);
    	for (int i = 0; i < tankArray.length; i++) {
    		drawPyramid((float) tankArray[i].getYStart(), (float) tankArray[i].getZStart(), (float) tankArray[i].getXStart(), triSize, gl);
    	}
    	gl.glPopMatrix();
    	//Moves slicer
    	gl.glTranslatef(sliceX, sliceZ, sliceY);
        gl.glRotatef(sliceXYAn, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(sliceXZAn, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(sliceYZAn, 1.0f, 0.0f, 0.0f);
        //Draws lines on slicer
        gl.glBegin(GL.GL_LINES);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 0, -MAP_SIZE / 4);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, MAP_SIZE / 4, 0);
        /*
        gl.glVertex3f(-MAP_SIZE  / 10, 0, 0);
        gl.glVertex3f(MAP_SIZE / 10, 0, 0);
        gl.glVertex3f(-MAP_SIZE  / 10, 0, MAP_SIZE / 4);
        gl.glVertex3f(MAP_SIZE  / 10, 0, MAP_SIZE / 4);
        */
        gl.glEnd();
        //Draws the slicer
    	gl.glBegin(GL.GL_QUADS);
        gl.glColor4f(0.0f,0.0f,0.0f,.75f);			// Set The Color
        /*
        float xArr[] = new float[4];
        float yArr[] = new float[4];
        float zArr[] = new float[4];
        for (int i = 0; i < 4; i++) {
        	xArr[i] = (float) mapCrArr[i].getX() + MAP_SIZE/2;
        }
        for (int i = 0; i < 4; i++) {
        	yArr[i] = (float) mapCrArr[i].getY() + MAP_SIZE/2;
        }
        for (int i = 0; i < 4; i++) {
        	zArr[i] = (float) mapCrArr[i].getZ();
        }
        gl.glVertex3f(xArr[0], zArr[0], yArr[0]);
        gl.glVertex3f(xArr[1], zArr[1], yArr[1]);
        gl.glVertex3f(xArr[2], zArr[2], yArr[2]);
        gl.glVertex3f(xArr[3], zArr[3], yArr[3]);
        */
        gl.glVertex3f(-MAP_SIZE / 2, 0, -MAP_SIZE / 2);
        gl.glVertex3f(-MAP_SIZE / 2, 0, MAP_SIZE / 2);
        gl.glVertex3f(MAP_SIZE / 2, 0, MAP_SIZE / 2);
        gl.glVertex3f(MAP_SIZE / 2, 0, -MAP_SIZE / 2);
        
        /*
        gl.glVertex3f((float) mapCrArr[0].getX(), (float) mapCrArr[0].getZ(), (float) mapCrArr[0].getY());
        gl.glVertex3f((float) mapCrArr[1].getX(), (float) mapCrArr[1].getZ(), (float) mapCrArr[1].getY());
        gl.glVertex3f((float) mapCrArr[2].getX(), (float) mapCrArr[2].getZ(), (float) mapCrArr[2].getY());		
        gl.glVertex3f((float) mapCrArr[3].getX(), (float) mapCrArr[3].getZ(), (float) mapCrArr[3].getY());			
        */
        
        gl.glEnd();				// Done Drawing The Quad
        //gl.glPopMatrix();
    	//Renders height map
        gl.glPushMatrix();
        gl.glLoadIdentity();                                 // Reset The Matrix
        //              Position         View      Up Vector
        glu.gluLookAt(MAP_SIZE / STEP_SIZE, MAP_SIZE / 4, MAP_SIZE * 1.25, MAP_SIZE / STEP_SIZE, 40 , 0, 0, 1, 0);// This Determines Where The Camera's Position And View Is
        //glu.gluLookAt(212, 60, 194, 186, 55, 171, 0, 1, 0);// This Determines Where The Camera's Position And View Is
        gl.glScalef(scaleValue, scaleValue * HEIGHT_RATIO, scaleValue);
        //gl.glPopMatrix();
        //Draws floor
        gl.glPushMatrix();
        gl.glTranslatef(MAP_SIZE / 2, 0, MAP_SIZE / 2);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor3f(0.588f,0.294f,0.0f);
        gl.glVertex3f(-MAP_SIZE / 2, 0, -MAP_SIZE / 2);
        gl.glVertex3f(-MAP_SIZE / 2, 0, MAP_SIZE / 2);
        gl.glVertex3f(MAP_SIZE / 2, 0, MAP_SIZE / 2);
        gl.glVertex3f(MAP_SIZE / 2, 0, -MAP_SIZE / 2);
        gl.glEnd();
        gl.glPopMatrix();
        //Renders height map
    	gl.glBegin(renderMode.getValue());
        for (int X = 0; X < (MAP_SIZE - STEP_SIZE); X += STEP_SIZE)
            for (int Y = 0; Y < (MAP_SIZE - STEP_SIZE); Y += STEP_SIZE) {
                // Get The (X, Y, Z) Value For The Bottom Left Vertex
                int x = X;
                int y = height(pHeightMap, X, Y);
                int z = Y;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);
                gl.glVertex3i(x, y, z);                          // Send This Vertex To OpenGL To Be Rendered (Integer Points Are Faster)
                
                // Get The (X, Y, Z) Value For The Bottom Right Vertex
                x = X + STEP_SIZE;
                y = height(pHeightMap, X + STEP_SIZE, Y);
                z = Y;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);
                gl.glVertex3i(x, y, z);                          // Send This Vertex To OpenGL To Be Rendered

                // Get The (X, Y, Z) Value For The Top Right Vertex
                x = X + STEP_SIZE;
                y = height(pHeightMap, X + STEP_SIZE, Y + STEP_SIZE);
                z = Y + STEP_SIZE;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);
                gl.glVertex3i(x, y, z);                          // Send This Vertex To OpenGL To Be Rendered
                
                // Get The (X, Y, Z) Value For The Top Left Vertex
                x = X;
                y = height(pHeightMap, X, Y + STEP_SIZE);
                z = Y + STEP_SIZE;

                // Set The Color Value Of The Current Vertex
                setVertexColor(gl, pHeightMap, x, z);

                gl.glVertex3i(x, y, z);	                         // Send This Vertex To OpenGL To Be Rendered

            }
        gl.glEnd();
      //Draws stick guy
    	float boxZ = 0;
    	for(int k = 0; k < pointArray.length; k++) {
    		boxZ +=  ((normalH * (1/pixelToCo)/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((0 - pointArray[k].getX()), 2) + Math.pow((pointArray[k].getY()-0), 2) + Math.pow((0 - pointArray[k].getZ()), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2))));
			//boxZ +=  ((100/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((MAP_SIZE / 2 - pointArray[k].getX()), 2) + Math.pow((pointArray[k].getY()- MAP_SIZE / 2), 2) + Math.pow((MAP_SIZE / 2 - pointArray[k].getZ()), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2))));
		}
    	//System.out.println(boxZ);
    	//double xChange;
		//double zChange = 0;
		/*
			double yChange = (MAP_SIZE / 2 - MAP_SIZE / 2) * pixelToCo;
			double xChange = (MAP_SIZE / 2 - MAP_SIZE / 2) * pixelToCo;
				for(int k = 0; k < pointArray.length; k++) {
					boxZ +=  ((100/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((xChange - pointArray[k].getX()), 2) + Math.pow((pointArray[k].getY()-yChange), 2) + Math.pow((zChange - pointArray[k].getZ()), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2))));
				}
		*/
    	//lkjh
    	boxZ -= 6;
    	gl.glTranslatef(MAP_SIZE / 2, 0, MAP_SIZE  / 2);
    	gl.glBegin(GL.GL_QUADS);           	// Draw A Quad
        gl.glColor3f(1.0f,1.0f,0.0f);			// Set The Color To Yellow
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Right Of The Quad (Top)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Left Of The Quad (Top)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Bottom Left Of The Quad (Top)
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Bottom Right Of The Quad (Top)

        //gl.glColor3f(1.0f,0.5f,0.0f);			// Set The Color To Orange
        gl.glVertex3f( 1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Top Right Of The Quad (Bottom)
        gl.glVertex3f(-1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Top Left Of The Quad (Bottom)
        gl.glVertex3f(-1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Left Of The Quad (Bottom)
        gl.glVertex3f( 1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Right Of The Quad (Bottom)

        //gl.glColor3f(1.0f,0.0f,0.0f);			// Set The Color To Red
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Right Of The Quad (Front)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Left Of The Quad (Front)
        gl.glVertex3f(-1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Left Of The Quad (Front)
        gl.glVertex3f( 1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Right Of The Quad (Front)

        //gl.glColor3f(1.0f,1.0f,0.0f);			// Set The Color To Yellow
        gl.glVertex3f( 1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Left Of The Quad (Back)
        gl.glVertex3f(-1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Right Of The Quad (Back)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Right Of The Quad (Back)
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Left Of The Quad (Back)

        //gl.glColor3f(0.0f,0.0f,1.0f);			// Set The Color To Blue
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Right Of The Quad (Left)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Left Of The Quad (Left)
        gl.glVertex3f(-1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Left Of The Quad (Left)
        gl.glVertex3f(-1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Right Of The Quad (Left)

        //gl.glColor3f(1.0f,0.0f,1.0f);			// Set The Color To Violet
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Right Of The Quad (Right)
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Left Of The Quad (Right)
        gl.glVertex3f( 1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Left Of The Quad (Right)
        gl.glVertex3f( 1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Right Of The Quad (Right)
      gl.glEnd();				// Done Drawing The Quad
      gl.glTranslatef(-MAP_SIZE / 2, 0, -MAP_SIZE  / 2);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);                // Reset The Color
        //gl.glPushMatrix();
        //gl.glPopMatrix();
        sliceXYAn = 0;
        sliceXZAn = 0;
        sliceYZAn = 0;
        sliceX = 0;
        sliceY = 0;
        sliceZ = 0;
    }

    private void setVertexColor(GL gl, byte[] pHeightMap, int x, int y) {                 // Sets The Color Value For A Particular Index, Depending On The Height Index
        float fColor = -0f + (height(pHeightMap, x, y) / 256.0f);
        // Assign This Blue Shade To The Current Vertex
        //gl.glColor3f(fColor - 255, fColor - 255, fColor);
        gl.glColor4f(.588f + colorFactor * fColor, .294f + colorFactor * fColor, colorFactor * fColor, .75f);
        //gl.glColor3f(colorFactor * .588f * fColor, 1, colorFactor * .294f * fColor);
    }

    private int height(byte[] pHeightMap, int X, int Y) {                         // This Returns The Height From A Height Map Index

        int x = X % MAP_SIZE;                                               // Error Check Our x Value
        int y = Y % MAP_SIZE;                                               // Error Check Our y Value
        return pHeightMap[x + (y * MAP_SIZE)] & 0xFF;                        // Index Into Our Height Array And Return The Height
    }

    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
    private void update() {
        if (zoomIn) {
            scaleValue += 0.001f;
            //areTri = true;
        }
        if (zoomOut)
            scaleValue -= 0.001f;
        if (isLeft == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].incX(incrementFactor);
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].incX(incrementFactor);
			}
			sliceX -= speed * incrementFactor / pixelToCo;
			//pt1XCo += pixelToCo;
			//pt2XCo += pixelToCo;
			/*
			for (int i = 0; i < 4; i++) {
				//mapCrArr[i].decrX(incrementFactor * pixelToCo);
				mapCrArr[i].incX(incrementFactor / pixelToCo);
			}
			*/
		}
		if(isRight == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].decrX(incrementFactor);
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].decrX(incrementFactor);
			}
			sliceX += speed * incrementFactor / pixelToCo;
			//pt1XCo -= pixelToCo;
			//pt2XCo -= pixelToCo;
			/*
			for (int i = 0; i < 4; i++) {
				//mapCrArr[i].incX(incrementFactor * pixelToCo);
				mapCrArr[i].decrX(incrementFactor / pixelToCo);
			}
			*/
		}
		if(isVin == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].decrZ(incrementFactor);
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].decrZ(incrementFactor);
			}
			sliceZ += speed * incrementFactor / pixelToCo;
			//pt1ZCo += pixelToCo;
			//pt2ZCo += pixelToCo;
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].incZ(incrementFactor / pixelToCo);
			}
			*/
		}
		if(isVout == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].incZ(incrementFactor);
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].incZ(incrementFactor);
			}
			sliceZ -= speed * incrementFactor / pixelToCo;
			//pt1ZCo -= pixelToCo;
			//pt2ZCo -= pixelToCo;
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].decrZ(incrementFactor / pixelToCo);
			}
			*/
		}
		if(isForward == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].incY(incrementFactor);
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].incY(incrementFactor);
			}
			sliceY -= speed * incrementFactor / pixelToCo;
			//pt1YCo -= pixelToCo;
			//pt2YCo -= pixelToCo;
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].decrY(incrementFactor / pixelToCo);
			}
			*/
		}
		if(isBackward == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].decrY(incrementFactor);
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].decrY(incrementFactor);
			}
			sliceY += speed * incrementFactor / pixelToCo;
			//pt1YCo += pixelToCo;
			//pt2YCo += pixelToCo;
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].incY(incrementFactor / pixelToCo);
			}
			*/
		}
		if(isYawR == true || isYawL == true) {
			for (int i = 0; i < pointArray.length; i++) {
				if(pointArray[i].getX() - xLocation * pixelToCo > 0) {
					pointArray[i].setXYAngle(Math.atan(pointArray[i].getY()/(pointArray[i].getX() - xLocation * pixelToCo)));
				}
				else if (pointArray[i].getX() - xLocation * pixelToCo < 0) {
					pointArray[i].setXYAngle(Math.atan(pointArray[i].getY()/(pointArray[i].getX() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					pointArray[i].setXYAngle(Math.PI / 2);
				}
				pointArray[i].setXYRadius(Math.sqrt(Math.pow((pointArray[i].getX() - xLocation * pixelToCo),2) + Math.pow(pointArray[i].getY(), 2)));
			}
			for (int i = 0; i < tankArray.length; i++) {
				if(tankArray[i].getX() - xLocation * pixelToCo > 0) {
					tankArray[i].setXYAngle(Math.atan(tankArray[i].getY()/(tankArray[i].getX() - xLocation * pixelToCo)));
				}
				else if (tankArray[i].getX() - xLocation * pixelToCo < 0) {
					tankArray[i].setXYAngle(Math.atan(tankArray[i].getY()/(tankArray[i].getX() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					tankArray[i].setXYAngle(Math.PI / 2);
				}
				tankArray[i].setXYRadius(Math.sqrt(Math.pow((tankArray[i].getX() - xLocation * pixelToCo),2) + Math.pow(tankArray[i].getY(), 2)));
			}
			/*
			for (int i = 0; i < 4; i++) {
				if(mapCrArr[i].getX() - xLocation * pixelToCo > 0) {
					mapCrArr[i].setXYAngle(Math.atan(mapCrArr[i].getY()/(mapCrArr[i].getX() - xLocation * pixelToCo)));
				}
				else if (mapCrArr[i].getX() - xLocation * pixelToCo < 0) {
					mapCrArr[i].setXYAngle(Math.atan(mapCrArr[i].getY()/(mapCrArr[i].getX() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					mapCrArr[i].setXYAngle(Math.PI / 2);
				}
				mapCrArr[i].setXYRadius(Math.sqrt(Math.pow((mapCrArr[i].getX() - xLocation * pixelToCo),2) + Math.pow(mapCrArr[i].getY(), 2)));
			}
			*/
		}
		
		if(isYawR == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].decrXYAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				pointArray[i].setX(pointArray[i].getXYRadius() * Math.cos(pointArray[i].getXYAngle()) + xLocation * pixelToCo);
				pointArray[i].setY(pointArray[i].getXYRadius() * Math.sin(pointArray[i].getXYAngle()));
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].decrXYAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				tankArray[i].setX(tankArray[i].getXYRadius() * Math.cos(tankArray[i].getXYAngle()) + xLocation * pixelToCo);
				tankArray[i].setY(tankArray[i].getXYRadius() * Math.sin(tankArray[i].getXYAngle()));
			}
			//sliceXYAn -= rotSpeed * incrementFactor * speed;
			sliceXYAn = -(float) (rotSpeed * incrementFactor * speed);
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].incXYAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				mapCrArr[i].setX(mapCrArr[i].getXYRadius() * Math.cos(mapCrArr[i].getXYAngle()) + xLocation * pixelToCo);
				mapCrArr[i].setY(mapCrArr[i].getXYRadius() * Math.sin(mapCrArr[i].getXYAngle()));
			}
			*/
		}
		if(isYawL == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].incXYAngle((rotSpeed * incrementFactor * 2 * Math.PI) / 360);
				pointArray[i].setX(pointArray[i].getXYRadius() * Math.cos(pointArray[i].getXYAngle()) + xLocation * pixelToCo);
				pointArray[i].setY(pointArray[i].getXYRadius() * Math.sin(pointArray[i].getXYAngle()));
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].incXYAngle((rotSpeed * incrementFactor * 2 * Math.PI) / 360);
				tankArray[i].setX(tankArray[i].getXYRadius() * Math.cos(tankArray[i].getXYAngle()) + xLocation * pixelToCo);
				tankArray[i].setY(tankArray[i].getXYRadius() * Math.sin(tankArray[i].getXYAngle()));
			}
			//sliceXYAn += rotSpeed * incrementFactor * speed;
			sliceXYAn = (float) (rotSpeed * incrementFactor * speed);
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].decrXYAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				mapCrArr[i].setX(mapCrArr[i].getXYRadius() * Math.cos(mapCrArr[i].getXYAngle()) + xLocation * pixelToCo);
				mapCrArr[i].setY(mapCrArr[i].getXYRadius() * Math.sin(mapCrArr[i].getXYAngle()));
			}
			*/
		}
		
		if(isRollR == true || isRollL == true) {
			for (int i = 0; i < pointArray.length; i++) {
				if(pointArray[i].getX() - xLocation * pixelToCo > 0) {
					pointArray[i].setXZAngle(Math.atan(pointArray[i].getZ()/(pointArray[i].getX() - xLocation * pixelToCo)));
				}
				else if (pointArray[i].getX() - xLocation * pixelToCo < 0) {
					pointArray[i].setXZAngle(Math.atan(pointArray[i].getZ()/(pointArray[i].getX() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					pointArray[i].setXZAngle(Math.PI / 2);
				}
				pointArray[i].setXZRadius(Math.sqrt(Math.pow((pointArray[i].getX() - xLocation * pixelToCo),2) + Math.pow(pointArray[i].getZ(), 2)));
			}
			for (int i = 0; i < tankArray.length; i++) {
				if(tankArray[i].getX() - xLocation * pixelToCo > 0) {
					tankArray[i].setXZAngle(Math.atan(tankArray[i].getZ()/(tankArray[i].getX() - xLocation * pixelToCo)));
				}
				else if (tankArray[i].getX() - xLocation * pixelToCo < 0) {
					tankArray[i].setXZAngle(Math.atan(tankArray[i].getZ()/(tankArray[i].getX() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					tankArray[i].setXZAngle(Math.PI / 2);
				}
				tankArray[i].setXZRadius(Math.sqrt(Math.pow((tankArray[i].getX() - xLocation * pixelToCo),2) + Math.pow(tankArray[i].getZ(), 2)));
			}
			/*
			for (int i = 0; i < 4; i++) {
				if(mapCrArr[i].getX() - xLocation * pixelToCo > 0) {
					mapCrArr[i].setXZAngle(Math.atan(mapCrArr[i].getZ()/(mapCrArr[i].getX() - xLocation * pixelToCo)));
				}
				else if (mapCrArr[i].getX() - xLocation * pixelToCo < 0) {
					mapCrArr[i].setXZAngle(Math.atan(mapCrArr[i].getZ()/(mapCrArr[i].getX() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					mapCrArr[i].setXZAngle(Math.PI / 2);
				}
				mapCrArr[i].setXZRadius(Math.sqrt(Math.pow((mapCrArr[i].getX() - xLocation * pixelToCo),2) + Math.pow(mapCrArr[i].getZ(), 2)));
			}
			*/
		}
		
		if(isRollL == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].decrXZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				pointArray[i].setX(pointArray[i].getXZRadius() * Math.cos(pointArray[i].getXZAngle()) + xLocation * pixelToCo);
				pointArray[i].setZ(pointArray[i].getXZRadius() * Math.sin(pointArray[i].getXZAngle()));
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].decrXZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				tankArray[i].setX(tankArray[i].getXZRadius() * Math.cos(tankArray[i].getXZAngle()) + xLocation * pixelToCo);
				tankArray[i].setZ(tankArray[i].getXZRadius() * Math.sin(tankArray[i].getXZAngle()));
			}
			//sliceXZAn += rotSpeed * incrementFactor * speed;
			sliceXZAn = +(float) (rotSpeed * incrementFactor * speed);
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].incXZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				mapCrArr[i].setX(mapCrArr[i].getXZRadius() * Math.cos(mapCrArr[i].getXZAngle()) + xLocation * pixelToCo);
				mapCrArr[i].setZ(mapCrArr[i].getXZRadius() * Math.sin(mapCrArr[i].getXZAngle()));
			}
			*/
		}
		if(isRollR == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].incXZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				pointArray[i].setX(pointArray[i].getXZRadius() * Math.cos(pointArray[i].getXZAngle()) + xLocation * pixelToCo);
				pointArray[i].setZ(pointArray[i].getXZRadius() * Math.sin(pointArray[i].getXZAngle()));
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].incXZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				tankArray[i].setX(tankArray[i].getXZRadius() * Math.cos(tankArray[i].getXZAngle()) + xLocation * pixelToCo);
				tankArray[i].setZ(tankArray[i].getXZRadius() * Math.sin(tankArray[i].getXZAngle()));
			}
			//sliceXZAn -= rotSpeed * incrementFactor * speed;
			sliceXZAn = -(float) (rotSpeed * incrementFactor * speed);
			/*
			for (int i = 0; i < 4; i++) {
				mapCrArr[i].decrXZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				mapCrArr[i].setX(mapCrArr[i].getXZRadius() * Math.cos(mapCrArr[i].getXZAngle()) + xLocation * pixelToCo);
				mapCrArr[i].setZ(mapCrArr[i].getXZRadius() * Math.sin(mapCrArr[i].getXZAngle()));
			}
			*/
		}
		
		if(isPitchU == true || isPitchD == true) {
			for (int i = 0; i < pointArray.length; i++) {
				if(pointArray[i].getY() - xLocation * pixelToCo > 0) {
					pointArray[i].setYZAngle(Math.atan(pointArray[i].getZ()/(pointArray[i].getY() - xLocation * pixelToCo)));
				}
				else if (pointArray[i].getY() - xLocation * pixelToCo < 0) {
					pointArray[i].setYZAngle(Math.atan(pointArray[i].getZ()/(pointArray[i].getY() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					pointArray[i].setYZAngle(Math.PI / 2);
				}
				pointArray[i].setYZRadius(Math.sqrt(Math.pow((pointArray[i].getY() - xLocation * pixelToCo),2) + Math.pow(pointArray[i].getZ(), 2)));
			}
			for (int i = 0; i < tankArray.length; i++) {
				if(tankArray[i].getY() - xLocation * pixelToCo > 0) {
					tankArray[i].setYZAngle(Math.atan(tankArray[i].getZ()/(tankArray[i].getY() - xLocation * pixelToCo)));
				}
				else if (tankArray[i].getY() - xLocation * pixelToCo < 0) {
					tankArray[i].setYZAngle(Math.atan(tankArray[i].getZ()/(tankArray[i].getY() - xLocation * pixelToCo)) + Math.PI);
				}
				else {
					tankArray[i].setYZAngle(Math.PI / 2);
				}
				tankArray[i].setYZRadius(Math.sqrt(Math.pow((tankArray[i].getY() - xLocation * pixelToCo),2) + Math.pow(tankArray[i].getZ(), 2)));
			}
		}
		
		if(isPitchU == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].decrYZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				pointArray[i].setY(pointArray[i].getYZRadius() * Math.cos(pointArray[i].getYZAngle()) + xLocation * pixelToCo);
				pointArray[i].setZ(pointArray[i].getYZRadius() * Math.sin(pointArray[i].getYZAngle()));
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].decrYZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				tankArray[i].setY(tankArray[i].getYZRadius() * Math.cos(tankArray[i].getYZAngle()) + xLocation * pixelToCo);
				tankArray[i].setZ(tankArray[i].getYZRadius() * Math.sin(tankArray[i].getYZAngle()));
			}
			//sliceYZAn -= rotSpeed * incrementFactor * speed;
			sliceYZAn = -(float) (rotSpeed * incrementFactor * speed);
		}
		if(isPitchD == true) {
			for (int i = 0; i < pointArray.length; i++) {
				pointArray[i].incYZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				pointArray[i].setY(pointArray[i].getYZRadius() * Math.cos(pointArray[i].getYZAngle()) + xLocation * pixelToCo);
				pointArray[i].setZ(pointArray[i].getYZRadius() * Math.sin(pointArray[i].getYZAngle()));
			}
			for (int i = 0; i < tankArray.length; i++) {
				tankArray[i].incYZAngle(rotSpeed * incrementFactor * (2 * Math.PI) / 360);
				tankArray[i].setY(tankArray[i].getYZRadius() * Math.cos(tankArray[i].getYZAngle()) + xLocation * pixelToCo);
				tankArray[i].setZ(tankArray[i].getYZRadius() * Math.sin(tankArray[i].getYZAngle()));
			}
			//sliceYZAn += rotSpeed * incrementFactor * speed;
			sliceYZAn = (float) (rotSpeed * incrementFactor * speed);
		}
    }

    public void display(GLAutoDrawable drawable) {
    	slice = slice();
        update();
        GL gl = drawable.getGL();

        // Clear Color Buffer, Depth Buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        /*
        gl.glLoadIdentity();                                 // Reset The Matrix
        //              Position         View      Up Vector
        glu.gluLookAt(212, 60, 194, 186, 55, 171, 0, 1, 0);// This Determines Where The Camera's Position And View Is
        gl.glScalef(scaleValue, scaleValue * HEIGHT_RATIO, scaleValue);
        */
        renderHeightMap(gl, slice);  // Render The Height Map
        
        for (int i = 0; i < MAP_SIZE; i++) {
        	for (int j = 0; j < MAP_SIZE; j++) {
        		//System.out.print(slice[j + MAP_SIZE * i] + " ");
        	}
        }
        
    }

    public void reshape(GLAutoDrawable drawable,
                        int xstart,
                        int ystart,
                        int width,
                        int height) {
        GL gl = drawable.getGL();

        height = (height == 0) ? 1 : height;

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(45, (float) width / height, 1, 1000);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }
    
    public byte[] slice() {
		byte[] mySlice = new byte[(int) (MAP_SIZE * MAP_SIZE)];
		double xChange;
		double zChange = 0;
		for (int i = 0; i < MAP_SIZE; i += STEP_SIZE) {
			double yChange = (i - MAP_SIZE / 2) * pixelToCo;
			for (int j = 0; j < MAP_SIZE; j += STEP_SIZE) {
				xChange = ( j - MAP_SIZE / 2) * pixelToCo;
				for(int k = 0; k < pointArray.length; k++) {
					mySlice[j + (int) MAP_SIZE * i] +=  normalH * (1/pixelToCo) * ((1/(Math.sqrt(2 * Math.PI) * Math.pow(Math.E,(Math.pow((xChange - pointArray[k].getX()), 2) + Math.pow((pointArray[k].getY()-yChange), 2) + Math.pow((zChange - pointArray[k].getZ()), 2) + Math.pow((pointArray[k].getHeightFactor()/ 2),2)) / 2))));
				}
			}
		}
		//for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
			//mySlice[i] += 75;
		//}
		return mySlice;
	}
    public void drawPyramid(float myX, float myZ, float myY, float triSize, GL gl) {
    	float y = (float) (myY / pixelToCo);
    	float z = (float) (myZ / pixelToCo);
    	float x = (float) (myX / pixelToCo);
    	/*
    	float y = myY;
    	float z = myZ;
    	float x = myX;
    	*/
    	gl.glPushMatrix();
    	gl.glTranslatef(y, z, x);
		gl.glBegin(GL.GL_TRIANGLES);		    // Drawing Using Triangles
        //gl.glColor3f(1.0f,0.0f,0.0f);			// Red
        gl.glVertex3f( 0.0f, triSize, 0.0f);			// Top Of Triangle (Front)
        //gl.glColor3f(0.0f,1.0f,0.0f);			// Green
        gl.glVertex3f(-triSize,-triSize, triSize);			// Left Of Triangle (Front)
        //gl.glColor3f(0.0f,0.0f,1.0f);			// Blue
        gl.glVertex3f( triSize,-triSize, triSize);			// Right Of Triangle (Front)
        //gl.glColor3f(1.0f,0.0f,0.0f);			// Red
        gl.glVertex3f( 0.0f, triSize, 0.0f);			// Top Of Triangle (Right)
        //gl.glColor3f(0.0f,0.0f,1.0f);			// Blue
        gl.glVertex3f( triSize,-triSize, triSize);			// Left Of Triangle (Right)
        //gl.glColor3f(0.0f,1.0f,0.0f);			// Green
        gl.glVertex3f( triSize,-triSize, -triSize);			// Right Of Triangle (Right)
        //gl.glColor3f(1.0f,0.0f,0.0f);			// Red
        gl.glVertex3f( 0.0f, triSize, 0.0f);			// Top Of Triangle (Back)
        //gl.glColor3f(0.0f,1.0f,0.0f);			// Green
        gl.glVertex3f( triSize,-triSize, -triSize);			// Left Of Triangle (Back)
        //gl.glColor3f(0.0f,0.0f,1.0f);			// Blue
        gl.glVertex3f(-triSize,-triSize, -triSize);			// Right Of Triangle (Back)
        //gl.glColor3f(1.0f,0.0f,0.0f);			// Red
        gl.glVertex3f( 0.0f, triSize, 0.0f);			// Top Of Triangle (Left)
        //gl.glColor3f(0.0f,0.0f,1.0f);			// Blue
        gl.glVertex3f(-triSize,-triSize,-triSize);			// Left Of Triangle (Left)
        //gl.glColor3f(0.0f,1.0f,0.0f);			// Green
        gl.glVertex3f(-triSize,-triSize, triSize);			// Right Of Triangle (Left)
        gl.glEnd();
        gl.glPopMatrix();
    }
    
    public void reset (GL gl) {
    	gl.glLoadIdentity();                                 // Reset The Matrix
        //              Position         View      Up Vector
        glu.gluLookAt(212, 60, 194, 186, 55, 171, 0, 1, 0);// This Determines Where The Camera's Position And View Is
        gl.glScalef(scaleValue, scaleValue * HEIGHT_RATIO, scaleValue);
    }
    /*
    public void drawBox(float myX, float myZ, float myY, float length, float width, float height, GL gl) {
    	float y = myY * STEP_SIZE + MAP_SIZE/2;
    	float z = myZ * STEP_SIZE;
    	float x = myX * STEP_SIZE + MAP_SIZE/2;
    	gl.glBegin(GL.GL_QUADS);           	// Draw A Quad
        gl.glColor3f(1.0f,1.0f,0.0f);			// Set The Color To Green
        gl.glVertex3f( x + length, y + height, z);			// Top Right Of The Quad (Top)
        gl.glVertex3f(x, y + height, z);			// Top Left Of The Quad (Top)
        gl.glVertex3f(x, y + height, z);			// Bottom Left Of The Quad (Top)
        gl.glVertex3f( x + length, y + height, 1.0f * boxWidth);			// Bottom Right Of The Quad (Top)

        //gl.glColor3f(1.0f,0.5f,0.0f);			// Set The Color To Orange
        gl.glVertex3f( 1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Top Right Of The Quad (Bottom)
        gl.glVertex3f(-1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Top Left Of The Quad (Bottom)
        gl.glVertex3f(-1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Left Of The Quad (Bottom)
        gl.glVertex3f( 1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Right Of The Quad (Bottom)

        //gl.glColor3f(1.0f,0.0f,0.0f);			// Set The Color To Red
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Right Of The Quad (Front)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Left Of The Quad (Front)
        gl.glVertex3f(-1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Left Of The Quad (Front)
        gl.glVertex3f( 1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Right Of The Quad (Front)

        //gl.glColor3f(1.0f,1.0f,0.0f);			// Set The Color To Yellow
        gl.glVertex3f( 1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Left Of The Quad (Back)
        gl.glVertex3f(-1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Right Of The Quad (Back)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Right Of The Quad (Back)
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Left Of The Quad (Back)

        //gl.glColor3f(0.0f,0.0f,1.0f);			// Set The Color To Blue
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Right Of The Quad (Left)
        gl.glVertex3f(-1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Left Of The Quad (Left)
        gl.glVertex3f(-1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Left Of The Quad (Left)
        gl.glVertex3f(-1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Right Of The Quad (Left)

        //gl.glColor3f(1.0f,0.0f,1.0f);			// Set The Color To Violet
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight,-1.0f * boxWidth);			// Top Right Of The Quad (Right)
        gl.glVertex3f( 1.0f * boxWidth, boxZ + boxHeight, 1.0f * boxWidth);			// Top Left Of The Quad (Right)
        gl.glVertex3f( 1.0f * boxWidth, boxZ, 1.0f * boxWidth);			// Bottom Left Of The Quad (Right)
        gl.glVertex3f( 1.0f * boxWidth, boxZ,-1.0f * boxWidth);			// Bottom Right Of The Quad (Right)
        gl.glEnd();				// Done Drawing The Quad
        gl.glTranslatef(-MAP_SIZE / 2, 0, -MAP_SIZE  / 2);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);                // Reset The Color
    }
    */
}

