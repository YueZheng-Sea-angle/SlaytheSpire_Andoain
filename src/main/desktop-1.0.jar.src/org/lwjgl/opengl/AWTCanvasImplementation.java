package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import org.lwjgl.LWJGLException;

interface AWTCanvasImplementation {
  PeerInfo createPeerInfo(Canvas paramCanvas, PixelFormat paramPixelFormat, ContextAttribs paramContextAttribs) throws LWJGLException;
  
  GraphicsConfiguration findConfiguration(GraphicsDevice paramGraphicsDevice, PixelFormat paramPixelFormat) throws LWJGLException;
}


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\AWTCanvasImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */