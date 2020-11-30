import java.applet.Applet;
import java.applet.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;
import java.awt.event.*;
import java.awt.*;

public class Paint extends Applet implements MouseMotionListener, MouseListener
{	 
	//Temp Variables to store values
	Shape tempShape;
	Vector<Shape> ArrShapes= new Vector<>();
	boolean PressedFlag = false; //to detect whether pressed or not -- True == Pressed / False == NotPressed
	int ShapeCode;  //to detect which shape will be drawn -- 1 == Oval , 2 == Line , 3 == Rectangle , 4== FreeHand , 5 == Eraser
	
	int ColorCode; //to detect which color will be drawn -- 1 == Red , 2 == Green , 3 == Blue
	boolean IsSolid = false; //Solid Shape or Not
	
	//FreeHand Temp Variables
	int startX , startY =0;
	int endX, endY;
	int x , y;
	
	//Colors Buttons
	Button RedColorBtn;
	Button GreenColorBtn;
	Button BlueColorBtn;
	
	//Shapes Buttons
	Button OvalBtn;
	Button LineBtn;
	Button RectBtn;
	
	//Freehand Button Shape Code = 4
	Button FreehandBtn;
	
	//Clear All Button
	Button ClearAllBtn;


	//Undo Button
	Button UndoBtn;
	
	//Eraser Button Shape Code = 5
	Button EraseBtn;
	
	//CheckBox Filled or Not
	Checkbox chkFilled;
	
	//initialization
	public void init() 
		{ 
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			
			RedColorBtn = new Button("RED");
			RedColorBtn.setBackground(Color.red);
			RedColorBtn.addActionListener (new ButtonsListener());
			add(RedColorBtn);
			
			GreenColorBtn = new Button("GREEN");
			GreenColorBtn.setBackground(Color.green);
			GreenColorBtn.addActionListener (new ButtonsListener());
			add(GreenColorBtn);
			
			BlueColorBtn = new Button("BLUE");
			BlueColorBtn.setBackground(Color.blue);
			BlueColorBtn.addActionListener (new ButtonsListener());
			add(BlueColorBtn);
			
			FreehandBtn = new Button("Free Hand");
			FreehandBtn.addActionListener (new ButtonsListener());
			add(FreehandBtn);
			
			
			LineBtn = new Button("Draw Line");
			LineBtn.addActionListener (new ButtonsListener());
			add(LineBtn);
			
			OvalBtn = new Button("Draw Oval");
			OvalBtn.addActionListener (new ButtonsListener());
			add(OvalBtn);
			
			RectBtn = new Button("Draw Rectangle");
			RectBtn.addActionListener (new ButtonsListener());
			add(RectBtn);
			
			
			ClearAllBtn = new Button("Clear All");
			ClearAllBtn.addActionListener (new ButtonsListener());
			add(ClearAllBtn);
			
			UndoBtn = new Button("Undo");
			UndoBtn.addActionListener (new ButtonsListener());
			add(UndoBtn);
			
			EraseBtn = new Button("Erase");
			EraseBtn.setBackground(Color.white);
			EraseBtn.addActionListener (new ButtonsListener());
			add(EraseBtn);
			
			chkFilled = new Checkbox("Filled");
			add(chkFilled);
			chkFilled.addItemListener(new CheckButtonListener());
			
		}
		
		
	//Shape Class (Parent)
	abstract public class Shape 
	{
		Point startPoint = new Point(0,0);
		Point pointEnd = new Point();
		int width=0, height=0;
		int ColorFlag ;
		boolean SolidFlag;
		
		//FREE HAND VARIABLES 
		public int prevX, prevY; 
		public int xDragging, yDragging;
		public Vector<Shape> Freehand = new Vector<Shape>();
		public int Fcolor;
		
		void setprevX(int x)
		{
			prevX = x;
		}
		
		int getprevX()
		{
			return prevX;
		}	
		
		void setprevY(int y)
		{
			prevY = y;
		}
		
		int getprevY()
		{
			return prevY;
		}	
		
		void setColorFlag(int c)
		{
			ColorFlag = c;
		}
		
		int getColorFlag()
		{
			return ColorFlag;
		}	
		
		void setEnd(Point p)
		{
			pointEnd = p;
		}
		
		Point getEnd()
		{
			return pointEnd;
		}	
		
		void setStart(Point p) 
		{
			startPoint = p;		
		}
		
		Point getStart()
		{
			return startPoint;
		}
		void setWidth(int w) 
		{
			width = w;		
		}
		
		int getWidth()
		{
			return width;
		}
		
		void setHeight(int h) 
		{
			height = h;		
		}
		
		int getHeight()
		{
			return height;
		}
		
		abstract void draw(Graphics g);
	}
	
	//Eraser Class
	public class MyEraser extends Shape 
	{
		public void draw(Graphics g)
		{
			g.setColor(Color.white);
			g.fillOval(startPoint.x, startPoint.y , width , height);
		}
		
	}
	//Oval Class
	public class MyOval extends Shape
	{
		public void draw(Graphics g)
		{
			if( ColorFlag == 1)
			{
				g.setColor(Color.red);
			}

			else if(ColorFlag ==2)
			{
				g.setColor(Color.green);			
			}

			else if(ColorFlag ==3) 
			{
				g.setColor(Color.blue);		
			}
			
			if(SolidFlag == false)
				g.drawOval(startPoint.x, startPoint.y , width , height);
			
			else if (SolidFlag == true)
				g.fillOval(startPoint.x, startPoint.y , width , height);	
			

		}
	}
	
	//Line Class
	public class MyLine extends Shape
	{
		public void draw(Graphics g)
		{
			if( ColorFlag == 1)
			{
				g.setColor(Color.red);
			}

			else if(ColorFlag ==2)
			{
				g.setColor(Color.green);			
			}

			else if(ColorFlag ==3) 
			{
				g.setColor(Color.blue);		
			}
			
			g.drawLine(startPoint.x,startPoint.y, pointEnd.x,pointEnd.y);
		}
	}	
	
	//Rectangle Class
	public class MyRectangle extends Shape
	{
		
		public void draw(Graphics g)
		{
			if( ColorFlag == 1)
			{
				g.setColor(Color.red);
			}

			else if(ColorFlag ==2)
			{
				g.setColor(Color.green);			
			}

			else if(ColorFlag ==3) 
			{
				g.setColor(Color.blue);		
			}
			
			if(SolidFlag == false)
				g.drawRect(startPoint.x, startPoint.y , width , height);
			
			else if(SolidFlag == true)
				g.fillRect(startPoint.x, startPoint.y , width , height);
		}
	}
	
	//Free Hand Class
	public class MyFreeHand extends Shape 
	{
		public void draw(Graphics g)
		{
			
			for (int i=0 ;i < Freehand.size(); i++ )
			{
				if( Freehand.get(i).Fcolor == 1)
				{
					g.setColor(Color.red);
				}

				else if(Freehand.get(i).Fcolor ==2)
				{
					g.setColor(Color.green);
	
				}

				else if(Freehand.get(i).Fcolor ==3) 
				{
					g.setColor(Color.blue);
				}
				g.drawLine(Freehand.get(i).prevX,Freehand.get(i).prevY, Freehand.get(i).xDragging,Freehand.get(i).yDragging);
				
			}
		}
	}
	
	//Action Listner to buttons
	class ButtonsListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (e.getSource() == RedColorBtn) 
					{
						ColorCode=1;
						tempShape.ColorFlag=1;
					}
				else if(e.getSource() == GreenColorBtn)
					{
						ColorCode=2;
						tempShape.ColorFlag=2;
					}
				else if(e.getSource() == BlueColorBtn)
					{
						ColorCode=3;
						tempShape.ColorFlag=3;
					}
				else if(e.getSource() == LineBtn)
					{
						tempShape = new MyLine();
						ShapeCode = 2;
					}
					
				else if(e.getSource() == OvalBtn)
					{
						tempShape = new MyOval();
						ShapeCode = 1;
					}
					
				else if(e.getSource() == RectBtn)
					{
						tempShape = new MyRectangle();
						ShapeCode = 3;
					}
					
				else if(e.getSource() == FreehandBtn)
					{
						tempShape = new MyFreeHand();
						ShapeCode = 4;
					}
				else if(e.getSource() == ClearAllBtn)
					{
						ArrShapes.clear();
						repaint();
					}
					
				else if(e.getSource() == EraseBtn)
					{
						tempShape = new MyEraser();
						ShapeCode =5;
					}
					
				else if(e.getSource() == UndoBtn )
					{
						if(ArrShapes.size() !=0 )
						{
							ArrShapes.remove(ArrShapes.size() - 1 );
							repaint();
						}
					}
   			}
	    }
  
	class CheckButtonListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e) 
		{               
			if(e.getStateChange()==1)
			{
				IsSolid = true;
				tempShape.SolidFlag = true;
			}
			else 
			{
				IsSolid = false;
				tempShape.SolidFlag = false;
			}
        }
	}
  
  
public void mousePressed(MouseEvent e)
	{
		PressedFlag = true;
		tempShape.startPoint = e.getPoint();
		tempShape.ColorFlag = ColorCode;
		tempShape.SolidFlag = IsSolid;
		
		x= e.getX();
		y= e.getY();
	}

			
public void mouseDragged(MouseEvent e)
	{
		    PressedFlag=false;

			if( ShapeCode == 1) //Oval Shape
			{
				tempShape.width+=4;
				tempShape.height+=2;
			}
			else if (ShapeCode == 2) //Line
			{
				tempShape.pointEnd = e.getPoint();
			}
			else if( ShapeCode == 3) //Rectangle Shape
			{
				tempShape.width+=4;
				tempShape.height+=2;
			}
			else if (ShapeCode == 5) //Eraser
			{
				tempShape.startPoint = e.getPoint();
				tempShape.width  = 20;
				tempShape.height = 20;
				ArrShapes.add(tempShape);
				tempShape = new MyEraser();	
				tempShape.startPoint = new Point();	
			}
			
			else if( ShapeCode == 4) //FreeHand
			{
				MyFreeHand f = new MyFreeHand();
				
				if(startX == 0 && startY == 0) 
				{
					startX = x;
					startY =y;
				}
				else
				{
					startX = endX;   
					startY = endY; 	
				}
			
				endX = e.getX();   // x-coordinate of mouse.
				endY = e.getY();   // y=coordinate of mouse.
				
				f.xDragging = endX;
				f.yDragging = endY;
				
				f.prevX = startX;
				f.prevY = startY;
				
				if( ColorCode == 1)
					{
						f.Fcolor =1;
					}

				else if(ColorCode ==2)
					{
						f.Fcolor =2;		
					}

				else if(ColorCode ==3) 
					{
						f.Fcolor =3;	
					}
			
				tempShape.Freehand.add(f); //Add it to the shapes Vector   		
			}
			
		repaint();
	}
	
public void mouseReleased(MouseEvent e) 
	{
		if(!PressedFlag)
		{
			ArrShapes.add(tempShape);
			if( ShapeCode == 1) //Oval Shape
			{
				tempShape = new MyOval();	
				tempShape.width= tempShape.height = 0;
			}
			else if (ShapeCode == 2) //Line
			{
				tempShape = new MyLine();			
			}
			else if( ShapeCode == 3) //Rectangle Shape
			{
				tempShape = new MyRectangle();	
				tempShape.width= tempShape.height = 0;
			}
			else if( ShapeCode == 5) //Eraser 
			{
				tempShape = new MyEraser();	
				tempShape.width= tempShape.height = 0;
			}
			else if( ShapeCode == 4) //Free Hand
			{
				startX = 0;  
				startY = 0;
			}
			
		}
	}
	
	public void paint(Graphics g)
	{
		if( ShapeCode == 1) //Oval Shape
			{
				tempShape.setStart(tempShape.startPoint);
				tempShape.setWidth(tempShape.width);
				tempShape.setHeight(tempShape.height);
				tempShape.draw(g);
				
			}
			
		else if (ShapeCode == 2) //Line
			{
				tempShape.setStart(tempShape.startPoint);
				tempShape.setEnd(tempShape.pointEnd);
				
				tempShape.draw(g);
			}
			
		else if( ShapeCode == 3) //Rectangle Shape
			{
				tempShape.setStart(tempShape.startPoint);
				tempShape.setWidth(tempShape.width);
				tempShape.setHeight(tempShape.height);
			
				tempShape.draw(g);
			}
			
		else if( ShapeCode == 5) //Eraser
			{
				tempShape.setStart(tempShape.startPoint);
				tempShape.setWidth(20);
				tempShape.setHeight(20);
			
				tempShape.draw(g);
			}
			
		else if( ShapeCode == 4) //Free Hand
			{
				tempShape.draw(g);
			}
		
		print(g);
		
	}
	
	public void print(Graphics g)
	{
		for(int i=0 ;i<ArrShapes.size() ;i++){
			ArrShapes.get(i).draw(g);  
		}
	}
	
	public void mouseMoved (MouseEvent e){}
	public void mouseClicked (MouseEvent e){}
	public void mouseEntered (MouseEvent e){}
	public void mouseExited (MouseEvent e){}
  
}