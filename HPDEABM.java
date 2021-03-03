package viraldynamic;
//In this model the number of infected cells increases according to the virus concentration 
//At first there are some infected cells that diffuse virus in domain
//There is a probability that infected cells die and if virus concentration in each cell
//is more than a random variable that cell gets infected. 
//In initial step there is no infected cell and virus comes from a desire boundary condition
//in each cell stochastically
import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.GridsAndAgents.PDEGrid2D;
import HAL.Gui.GridWindow;
//import HAL.Gui.UILabel;
//import HAL.Gui.UIWindow;
//import HAL.Gui.UIGrid;
//import HAL.Gui.GifMaker;
import HAL.Tools.FileIO;
import HAL.Rand;
//import HAL.Util;
//import LEARN_HERE.TumourClub.Cells;
import viraldynamic.Cells;
import viraldynamic.HPDEABM;

//import java.awt.image.RenderedImage;
import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Random;

//import javax.imageio.ImageIO;

import static HAL.Util.*;

public class HPDEABM extends AgentGrid2D<Cells>{     // Agents
public PDEGrid2D Virus;
public Rand rn;

public double[] Virusconcentr = new double[length];

public  double propHealthy = 0.9995;

public double MAX_PDE_STEP = 1;
public double Threshold = 0.000001;

public  double consumrateVirus = 2 * Math.pow(10, -2),
VirMax = 4 * Math.pow(10, -2);
//day
public  double diffcoeff = 2.2*Math.pow(10,-1); //to add
public  double infectionrate = 1 * Math.pow(10,-6);
public double deathprob = 8 * Math.pow(10,-3);

public HPDEABM(int xDim, int yDim, Rand rn){   // Constructor
   super(xDim, yDim, Cells.class);    // The last argument is a class type it uses  
   this.rn = rn;
   Virus = new PDEGrid2D(xDim, yDim);
   Virus.Update();
}


public static void main(String[] args) {
   int y = 200, x = 200, visScale = 2;
   int Nt = 1000;//5000;
   int msPause = 0;


   //output file setup
   // TIME STAMP
   java.util.Date now = new java.util.Date();
   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
   String date_time = dateFormat.format(now);
   
   // PATHS
   String projPath=PWD()+"/viraldynamic";
   String output_dir = projPath + "/output/" + date_time + "/";
   
   // CREATE OUTPUT DIR
   new File(output_dir).mkdirs();


   //save data
   GridWindow win = new GridWindow("Model: cells, Virus", x*2, y, visScale,true);
   FileIO paramfile = new FileIO(output_dir.concat("/").concat("Param").concat(".csv"), "w");
   FileIO outfile = new FileIO(output_dir.concat("/").concat("Out").concat(".csv"), "w");

   HPDEABM model =new HPDEABM(x,y,new Rand(1));
//   GifMaker gm=new GifMaker(output_dir.concat("/").concat("test").concat(".JPG"),100,true);

   
   //save parameter values to the file
   paramfile.Write("Parameters: \n propHealthy, consumrateVirus, diffcoeff \n");
   paramfile.Write(model.propHealthy + "," + model.consumrateVirus + "," + model.diffcoeff + "\n");
   outfile.Write("tick, HealthyCells, InfectedCells, DeadCells, VirusSummation \n");
   
   
   model.InitHPDEABM(x); // Put X For defining boundaries
   model.DrawModel(win);
//   gm.AddFrame(win);
   win.ToPNG("Initial.jpg");

   double[] pops = model.CountCells();
   System.out.println(pops[0]+", " + pops[1]+", " + pops[2]);
   
   int i = 60;
   int day = 1;
   for (int tick = 0; tick < Nt; tick ++){
       System.out.println(tick);
       if(win.IsClosed()) {
       	break;
       }
       
       win.TickPause(msPause);
       model.ModelStep(tick);
       model.DrawModel(win);
       
       if(tick == i) {
           win.ToPNG("Day"+day+"out.jpg");
           i = i + 60;
           day = day +1;
        }
       
//       gm.AddFrame(win);


       double summ = model.sum();
       double[] cellcount = model.CountCells();
       outfile.Write(tick +"," + cellcount[0] + "," + cellcount[1]+ "," + cellcount[2] + "," + summ+ "\n");

   }
   double[] pops2 = model.CountCells();
   System.out.println(pops2[0]+", " + pops2[1]+", " + pops2[2]);
   
//   gm.Close();
   outfile.Close();
   paramfile.Close();
   win.Close();
}


double[] CountCells(){ //count each cell type
   double HealthyCells = 0, InfectedCells = 0, DeadCells = 0; 
   double[] cellcount = new double[3];
   for (Cells cell: this){
       if (cell.CellType == 0){
       	HealthyCells+=1;
       }
       else if (cell.CellType == 1 ){
       	InfectedCells+=1;
       }
       else if (cell.CellType == 2){
           DeadCells += 1;
       }

   }
   cellcount[0] = HealthyCells;
   cellcount[1] = InfectedCells;
   cellcount[2] = DeadCells;

   return cellcount;
}


public Random rnd = new Random();
double randomGenerator() {
   return rnd.nextDouble();
}


public void InitHPDEABM(int x){
   //initiate the tumour: populate the grid with different cell types
   for (int i = 0; i < length; i++){
       double randv = randomGenerator();
//  	 double randv = rn.Double();
       if (randv < propHealthy){
           Cells c = NewAgentSQ(i);
           c.CellInit(true,false,false);
       }
       else if (randv >= propHealthy) {
           Cells c = NewAgentSQ(i);
           c.CellInit(false,true,false);
       }
       
   }


}

public void ModelStep(int tick){

   //update concentration within the InfectedCells
   double ViruConcentration = VIRprducRate(tick);
   for (Cells cell : this){
  	 
       if (cell.CellType == 1){ 
      	double Virnow = Virus.Get(cell.Isq());
      	double P = ViruConcentration + Virnow;
       	Virus.Set(cell.Isq(), P);
       }
   }
   int j = 0;
   for (int i = 0; i < MAX_PDE_STEP; i++) {

  	 j = j + 1;
   Virus.DiffusionADI(diffcoeff);

   if (Virus.MaxDelta() < Threshold) {
  	 Virus.Update();
  	 break;
   }
   
   Virus.Update();
}

   
   //Virus Decrease in each time step in all cells 
   for (Cells cell : this){
       double Virusnow = Virus.Get(cell.Isq());
       Virus.Add(cell.Isq(), -consumrateVirus*Virusnow);
   }
   Virus.Update();

   //CELL STEP
   for (Cells cell : this){
   		cell.CellStep();
   }
   for (Cells cell : this) {
   	cell.Celldeath();
   }
}

double sum() {
   double summ=0;
   for (int i=0; i<length; i++){
       Virusconcentr[i] =	Virus.Get(i);
   }
   for (double num : Virusconcentr ){
   	summ = summ + num;
   }        	
return summ;  
}

double VIRprducRate(int tick){

   double VPR;

   VPR=VirMax;

   return VPR;

}

public void DrawModel(GridWindow vis){
   for (int i = 0; i < length; i++) {
       Cells drawMe = GetAgent(i);
       if (drawMe == null){
	           vis.SetPix(i, RGB256(255, 255, 255));
	       }
	     else{
	    	 if (drawMe.CellType == 0){		// Healthy cells are blue
	    		 vis.SetPix(i, RGB256(0, 255, 51));
	    	 }
	    	 else if (drawMe.CellType == 1){   // Infected cells are red
	    		 vis.SetPix(i, RGB256(255, 0, 0));
	    	 }
	    	 else if (drawMe.CellType == 2){
	    		 vis.SetPix(i, RGB256(0, 0, 0));
	    	 }

	    	 vis.SetPix(ItoX(i)+xDim, ItoY(i),HeatMapRGB(Virus.Get(i)));
	     }
   }
   
}


}


class Cells extends AgentSQ2Dunstackable<HPDEABM>{
int CellType;

//CELL TYPE: 0 - Healthy, 1 - Infected, 2 - Deadcells

public void CellInit(boolean isHealthy, boolean isInfected, boolean isVirus){
   if(isHealthy == true){
       this.CellType = 0;
   }
   else if(isInfected== true){
       this.CellType = 1;
   }
   else if(isVirus== true) {
       this.CellType = 2;
   }
}

public void CellStep(){

	 
	double VIRnow = G.Virus.Get(Isq()); 

	double p = G.infectionrate*G.xDim*G.yDim* VIRnow;
//	if (G.rn.Double() < p) {
	if (G.randomGenerator() < p) {

		if (this.CellType == 0){
			this.CellType = 1;
		}
	}
}
public void Celldeath(){

   if (this.CellType == 1) {
   	
//	   if(G.rn.Double() < G.deathprob){
       if(G.randomGenerator() < G.deathprob){
  	
       	this.CellType = 2;
       }
   
   }
}


}


