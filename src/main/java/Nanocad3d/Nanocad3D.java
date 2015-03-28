package Nanocad3d;

//   Paramcad3D (September 2010)
//
//   R. Michael Sheetz, PhD 
//   Center for Computational Sciences
//   326A McVey Hall
//   University of Kentucky 
//   Lexington, Kentucky

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.*;
import java.util.regex.*;
import java.lang.Object.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.awt.Graphics2D;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.ImageIcon;
import net.java.games.jogl.*;
import net.java.games.jogl.util.*;

import Nanocad3d.Nanocad3D.menuMouseListener;
import Nanocad3d.xyplotgui.utils.monitorSubmenuMouseListener;

public class Nanocad3D extends JFrame
{

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
//                                                                                                                   //
//  Directory specifications:                                                                                        //
//                                                                                                                   //
//       // For older Dell computer in office                                                                        //
//       String paramchemDir = "C:\\PARAMCHEM\\Nanocad3D\\";                                                         //
//       String cgenffDir = "C:\\PARAMCHEM\\cgenff_win\\";                                                           //
//                                                                                                                   //
//                                                                                                                   //


//     // For new Dell computer  in office                                                                           //
//       String paramchemDir = "C:\\Users\\RMSheetz\\Documents\\PARAMCHEM\\Paramcad\\";                              //
//       String cgenffDir = paramchemDir + "cgenff_win\\";                                                           //

//                                                                                                                   //
//                                                                                                                   //
//     // For HP laptop                                                                                              //
//       String paramchemDir = "C:\\PARAMCHEM\\Paramcad\\";                                                          //
//       String cgenffDir = paramchemDir + "cgenff_win\\";                                                           //
//                                                                                                                   //
//                                                                                                                   //
                                                                                                                     //
       File directory = new File("");                                                                                //

       String fsep = System.getProperty("file.separator");

       String absPath = new String(directory.getAbsolutePath());                                                     //
//       String paramchemDir = new String(absPath.substring(0, absPath.lastIndexOf("\\")) + "\\");                   // 
       //String paramchemDir = new String(absPath.substring(0, absPath.lastIndexOf(fsep)) + fsep);                            //
       	String paramchemDir = absPath + fsep; /* above line is replaced with current line */                            													//

       String cgenffDir = paramchemDir + "cgenff_win\\";                                                             //
                                                                                                                     //
//       String mol2StructureFileDir = paramchemDir + "Structures\\" + "Molecules\\";                                //
String mol2StructureFileDir = paramchemDir + "Structures" + fsep + "Molecules" + fsep;
//       String molecularFragmentFileDir = paramchemDir + "Structures\\" + "Fragments\\";                            //
String molecularFragmentFileDir = paramchemDir + "Structures" + fsep + "Fragments" + fsep; 
//       String strMolecularFragmentFileDir = paramchemDir + "Structures\\" + "Fragments\\";                         //
String strMolecularFragmentFileDir = paramchemDir + "Structures" + fsep + "Fragments" + fsep; 
//       String strFunctionalGroupFileDir = paramchemDir + "Structures\\" + "Functional Groups\\";                   //
String strFunctionalGroupFileDir = paramchemDir + "Structures" + fsep + "Functional Groups" + fsep;
//       String functionalGroupFileDir = paramchemDir + "Structures\\" + "Functional Groups\\";                      //
String functionalGroupFileDir = paramchemDir + "Structures" + fsep + "Functional Groups" + fsep;
//       String strTmpUserDefStructureDir = paramchemDir + "tmpUserDefinedStructure\\";                              //
String strTmpUserDefStructureDir = paramchemDir + "tmpUserDefinedStructure" + fsep;   
       String strUserDefStructureDir = paramchemDir + "Structures\\" + "User Defined Structures\\";                  //    
       String userDefStructureDir = paramchemDir + "Structures\\" + "User Defined Structures\\";                     // 
       String strSolvatedMoleculesDir = paramchemDir + "Structures\\" + "Solvated Molecules\\";                      //
       String gaussianH2OFilesDir = paramchemDir + "Structures\\" + "Gaussian Files" + "Gaussian Water Files\\";     //
       String gaussianOptFreqDir = paramchemDir + "Structures\\" + "Gaussian Files" + "Gaussian Opt_Freq Files\\";   //
                                                                                                                     //
       File tmpUserDefStructureDir, solvatedMoleculesDir, solvatedAtomsDir;                                          //
       File currDir, solvatedGaussianFilesDir, gaussianDir1, gaussianDir2;                                           //
       File currentMol2File;                                                                                         //
//                                                                                                                   //
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                     //
//       String mol2StructureFileDir = paramchemDir + "Structures\\" + "Molecules\\";                                  //
//       String molecularFragmentFileDir = paramchemDir + "Structures\\" + "Fragments\\";                              //
//       String strMolecularFragmentFileDir = paramchemDir + "Structures\\" + "Fragments\\";                           //
//       String strFunctionalGroupFileDir = paramchemDir + "Structures\\" + "Functional Groups\\";                     //
//       String functionalGroupFileDir = paramchemDir + "Structures\\" + "Functional Groups\\";                        //
//       String strTmpUserDefStructureDir = paramchemDir + "tmpUserDefinedStructure\\";                                //
//       String strUserDefStructureDir = paramchemDir + "Structures\\" + "User Defined Structures\\";                  //    
//       String userDefStructureDir = paramchemDir + "Structures\\" + "User Defined Structures\\";                     // 
//       String strSolvatedMoleculesDir = paramchemDir + "Structures\\" + "Solvated Molecules\\";                      //
//       String gaussianH2OFilesDir = paramchemDir + "Structures\\" + "Gaussian Files" + "Gaussian Water Files\\";     //
//       String gaussianOptFreqDir = paramchemDir + "Structures\\" + "Gaussian Files" + "Gaussian Opt_Freq Files\\";   //
//                                                                                                                     //
//       File tmpUserDefStructureDir, solvatedMoleculesDir, solvatedAtomsDir;                                          //
//                                                                                                                     //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

     public static GLCanvas glcanvas = null;

     GL gl;
     GLU glu;
     GLUquadric quadric;
     GLUT glut = new GLUT();
     GLDrawable gldraw;

     int j, k, index, indx, ndx, ndx1, ndx2, atomIndex;
     int canvasWidth, canvasHeight, minCanvasDimen;
     int cnvsWidth, cnvsHeight;
     int nmenus, npopupmenus, nsubmenus, natoms, nfragmentAtoms, nfragatoms, nfragbonds;
     int mouseButtonPressed, mouseButtonClicked, attributeModified;
     int mbHeight, mbWidth, mbx, mby;
     int scaleRadius;
     int natomsSketched, natomsSelected, numAtoms, natomCutoff;
     int XX, YY;
     int elementNdx, keyNdx;
     int ndimensions, ncolors, ndims, ncoords, nbondvectors;
     int bondOrder, rotType, rotOriginNdx;
     int nselectableElements, nfragments, fragmentNum, fragmentID, maxfragID, nsubstructures, nbonds, nbondVectors, ncoordCols, nbvCols, nnewbvCols, nbvRows, nbndvecRows, nbndvectCols;
     int mode = GL.GL_RENDER;
     int nhits, selectMode, selectedAtomNdx, unselectedAtomNdx, nameNdx, elemNdx, selectedAtomName, selectedName, nameSelected, rotationOriginIndex;
     int targetAtomNdx, bondedAtomNdx, selectedFragbvIndex;
     int bufferSize;
     int nclicks, nmouseClicks, numMouseClicks, nrowsSelected;
     int ncurrentRows;
     int atomnumbr;
     int strlngth, fileSepNdx, dotExtNdx;
     int NAME;
     int fragFileID, nuserDefMolStructures;
//     int userDefFileNumbr;
     int userDefFileSuffix;
     int[] atomicNumber;
     int[] AtmNbr;
     int[] selectBuffer;
     int[] viewport;
     int[][] atomConnectivity;
     int[][] fragatomConnectivity;
     int[][] bondConnectivity;
     int[][] bvConnections;

     double menu1_x, menu1_y, menu2_x, menu2_y;
     double covrad;
     double mouseX, mouseY, mouseZ;
     double height, width, panelHeight, panelWidth, aspect;   
     double fov, near, far;
     double radius, bondRad;
     double R, G, B;
     double K;
     double x0, y0, z0;                  // origin of Cartesian reference frame
     double x, y, z, x1, y1, x2, y2 ;
     double coordx, coordy, coordz;
     double max_x, max_y, max_z, min_x, min_y, min_z;
     double delx, dely, delz, delta;
     double alpha, beta, omega, epsilon, eta, theta, phi, psi, delAlpha, delBeta, delOmega, delEpsilon, delEta, delTheta, delPhi, delPsi, delMu;
     double theta0, phi0, psi0, alpha0, beta0, omega0, eta0, mu0;
double deltaAngle;

double mux, muy, muz;

double nxx, nyy, nzz;
double nxx1, nxx2, nxx3, nyy1, nyy2, nyy3, nzz1, nzz2, nzz3;

     double delDihedralAngle, delAxialAngle, rotAccel, msfAccel, zloc;
     double deltaEpsilon, deltaEta, deltaDihedral;
     double axisSelectionDimen;
     double canvas2frameRatio, maxDisplayDimen, molScalingFactor;
     double displayWidth, displayHeight, maxMolecularDimen;
     double defaultAtomRadius, unscaledAtomRadius, bondRadius, scaleFactor, bondParam; 
     double dx, dy, dz, r, bl;    
     double ar1, ar2, cr1, cr2;  
     double defaultBondLength, bondDistance;
     double atomRadius, atomRad, atmRad;
     double ang2pixls;
     double bondLngth, bondAngle, dihedralAngle, initBondLngth, initBondAngle, initDihedralAngle;
     double bvConnTolAngle, bondConnTolAngle;
     double maxMolDimen;
     double molScaleFactor, canvasScaleFactor, combScaleFactor;
     double[] bondAngleRotationAxis;
     double[] dihedralAngleRotationAxis;
     double[] axialRotationAxis;

double[] globalRotationAxis;
double[] globalRotationAxes;

     double[] mouseDownCoords;
     double[] mouseUpCoords;
     double[] atomCoords;
     double[] delCoords;
     double[] delMouseXY;
     double[] rotOrigin;
     double[] radiusScalingFactor;
     double[] scaledAtomRadius;
     double[] molDimen;
     double[] x_axisLen;
     double[] y_axisLen;
     double[] z_axisLen;
     double[][] connectivity;
     double[][] axisEndPoints1;
     double[][] axisEndPoints2;
     double[][] atomCoordinates;
     double[][] fragatomCoordinates;
     double[][] origAtomCoordinates;
     double[][] zmatBondLength;
     double[][] bondLength;
     double[][] fragbondLength;
     double[][] bondVectors;
     double[][] fragbondVectors;
     double[][] newBondVectors;

     float bkg_red, bkg_green, bkg_blue, red, green, blue;
     float color_alphaValue, material_alphaValue;
     float[] atomColors;
     float[] bondMat1;
     float[] bondMat2;
     float[] material;
     float[][] elementRGB;
     float[][] RGB;
  
     long t_firstClick, t_secondClick, msec1, msec2;

     boolean sketchAtom, drawAtom, sketchBond, drawBond, sketchFcnalGrp, drawFcnalGrp, replaceAtom, adjustBondLngth, adjustBonAngle, adjustDihedralAngle, moveCoordOrigin;
     boolean drawAtomMode, editMoleculeMode, drawMode, editMode, editModeOn, displayMode, displayFragment, importStructureFile;
     boolean joinFragments, displayStructure;
     boolean arrowKeyDown, altKeyDown, cntrlKeyDown, deleteKeyDown, escapeKeyDown, shiftKeyDown, spaceKeyDown, tKeyDown, xKeyDown, yKeyDown, zKeyDown;
     boolean selectedElement, uniqueFragmentID;
     boolean x_rot, y_rot, z_rot;
     boolean displayGeom, retrievedGeom, displayMolecule, recomputeConnectivity;
     boolean bondsVisible, bondsColored;
     boolean inputFileRead, readXYZFile;
     boolean drawBondVectors;
     boolean pressmouse, releasemouse, timerRunning, eraseMolecule, selectionComplete;
     boolean RMBclicked;
     boolean writeBondLength, isBondLength, isBondAxis, isBondAngle, isDihedralAngle; 
     boolean bondVectorSelected, bondAngleSelected, dihedralAngleSelected, bvDihedralSelected, axialRotationSelected;
     boolean addBond, addElement, addFragment, addFcnalGrp;
     boolean translateAtoms, translateFragment, draggedMouse, mouseWasDragged, fragmentSelected, rotFragment, rotBondAngle;
     boolean releasedMouse;
     boolean xyzKeyDown;
     boolean bondLengthTxtFldSelected, bondAngleTxtFldSelected, dihedralTxtFldSelected;
     boolean compatBndOrdr;
     boolean undo, redo, deleteTmpUserStructureFiles;
     boolean forcefieldSelected, paramFrameVisible;
     boolean addSolventH2O;

//boolean globalAxisRotation, xyzKeysDepressed, rotatedStructure;
boolean globalAxisRotation, rotatedStructure;

     String filename, mol2filename, tmpMol2filename, currentMol2Filename, bvfilename, moleculeName;
     String strGeom;
     String displayStyle, coordFormat;
     String elementId, elementSelected, selectedElem, fcnlGrpSelected;
     String hybridization;
     String strval, elemText, strBondLength, strInitBondLength, strBondAngle, strInitBondAngle, strDihedralAngle, strInitDihedralAngle, dihedralBondFieldTxt;
     String elemFieldText;
     String selectedForceField;
     String mol2Heading1, mol2Heading2, mol2Heading3, mol2Heading4;
     String str;
     String[] atomID;
     String[] atomicSymbol = {"H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", 
                              "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb",
                              "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", 
                              "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta",
                              "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", 
                              "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt",
                              "Ds", "Rg"};

     ArrayList<Integer> selectedNames;
     ArrayList<Integer> selectedFragmentAtoms;
     ArrayList<Integer> fragmentAtoms;
     ArrayList<Integer> selectedFragmentArray;
     ArrayList<Integer> selectedFragments;
     ArrayList<Integer> AN;
     ArrayList<Integer> orderedAtomListIndices;
     ArrayList<Integer> xyzKeys;
     ArrayList<Integer> bvID;
     ArrayList<Integer> translatedAtomIndices;
     ArrayList<Integer> selectedComponents;
     ArrayList<Integer> bondAngleFragments1;
     ArrayList<Integer> bondAngleFragments2;
     ArrayList<ArrayList<Integer>> bondAngleFragments;
     ArrayList<Integer> dihedralAngleFragments;
     ArrayList<Integer> axialFragmentAtoms;
     ArrayList<Integer> fragment1BV;
     ArrayList<Integer> fragment2BV;
     ArrayList<Integer> fragmentBV;
     ArrayList<Integer> userDefnbvs;
     ArrayList<Double> coords;
     ArrayList<Double> fragmentOrigin;
     ArrayList<Double> atomRadii;
     ArrayList<Double> rotationAxis;
     ArrayList<Double> atomCharges;
     ArrayList<Double> usrdefTheta; 
     ArrayList<Double> usrdefPhi;
     ArrayList<Double> usrdefPsi;
     ArrayList<Float> newColor;
     ArrayList<String> elemID;
     ArrayList<String> fragElemID;
     ArrayList<String> componentElements;
     ArrayList<String> orderedElements;
     ArrayList<String> orderedAtomList;
     ArrayList<String> atomNames;
     ArrayList<String> atomTypes;
     ArrayList<String> moleculeRTI; 
     ArrayList<String> atomRTI;
     ArrayList<String> bondRTI;
     ArrayList<String> substructRTI;

//     ArrayList<String> QPenalty;
//     ArrayList<String> bondPenalty;
//     ArrayList<String> anglePenalty;
//     ArrayList<String> dhPenalty;
//     ArrayList<String> imprPenalty;

     ArrayList<Double> QPenalty;
     ArrayList<Double> bondPenalty;
     ArrayList<Double> bondAnglePenalty;
     ArrayList<Double> dhPenalties;
     ArrayList<Double> imprPenalty;

String theString; 
     String strDHAtmTypTxtArea;
     JTextArea dhAtmTypTxtArea;

////////
//
// GUI

ArrayList<Integer> albondID;
ArrayList<Double> alKb;
ArrayList<Double> alR0;
ArrayList<String> alAtomNames;
ArrayList<String> alAtomTypes;

ArrayList<String> bondAtomTypes;
ArrayList<String> angleAtomTypes;
ArrayList<String> dhAtomTypes;
ArrayList<String> imprAtomTypes;

ArrayList<String> dhParamSource;
ArrayList<Integer> dhMult;
ArrayList<Double> dhKforce;
ArrayList<Double> dhPhaseAngle;

//ArrayList<ArrayList<String>> atomNames_bonds;
//ArrayList<ArrayList<String>> atomTypes_bonds;
ArrayList<String> atomName1_bonds; ArrayList<String> atomName2_bonds;
ArrayList<String> atomType1_bonds; ArrayList<String> atomType2_bonds;
ArrayList<String> Hacceptors;

ArrayList<JMenu> structureSubmenus = new ArrayList<JMenu>();
ArrayList<JMenuItem> structureMenuItems = new ArrayList<JMenuItem>();

boolean hbDonor, hbAcceptor, atomSolvated;
boolean trigonalplanar, linear;
boolean idCentralDhBond;

int hbDonorNdx, hbAcceptorNdx, currentlySelectedAtomNdx;
int solventOrientNum;                // solvent molecule orientation number
int smonMax;                         // maximum number of solvent molecule orientations

int nbondAngles, ndihedrals;

int tableRowCount;
int mouseTblClicks;

int[] solvatedAtomIndices;
int[] dhRowsSelected;

int[][] arrBondAngleAtoms;
int[][] arrDihedralAtoms;

double[][] h2oCoordinates;

String hbDonorID, hbAcceptorID;

String keyID, rotationAxisID, rotAxis;

//////

     ArrayList<ArrayList> alGeom;
     ArrayList <ArrayList<String>> alAtomRTI;
     ArrayList <ArrayList<String>> alBondRTI;

     String displayedFile;

     Object selectedFF;

     java.util.Timer timer;

     IntBuffer selectionBuffer;

     Dimension screenSize, frameSize;

     JMenuBar menuBar;

     // Define menus
     JMenu menu, fileMenu, editMenu, viewMenu, structureMenu, displayMenu, settingsMenu, helpMenu;
     JMenu exportOptionsSubmenu, importOptionsSubmenu, importSubmenu, exportSubmenu, displayOptionsSubmenu, showBondsSubmenu, displayMOSubmenu, animationSubmenu, outputdataSubmenu;
     JMenu createMoleculeSubmenu, buildNanostructureSubmenu, buildPolymerSubmenu, buildCrystalSubmenu;  
     JMenu newStuctureSubmenu, drawAtomSubmenu, sketchFcnalGrpSubmenu, drawFcnalGrpSubmenu, drawFragmentSubmenu, createNanostructureSubmenu;

     JMenuItem newStuctureItem, drawAtomItem, drawFcnalGrpItem, drawFragmentItem, createNanostructureItem, createPolymerItem, createCrystalItem;

////////////
//
//  GUI:

JMenu forceFieldMenu, parametersMenu, runMenu, computeMenu;
JMenu empiricalSubmenu, semiempiricalSubmenu, coarseGrainedSubmenu, manyBodySubmenu, specialFFSubmenu, userDefinedSubmenu;
JMenu charmmSubmenu, oplsSubmenu;
JMenu class1additiveSubmenu, class2additiveSubmenu, polarizableFFSubmenu, charmmpolFFSubmenu, reactiveFFSubmenu, otherFFSubmenu;
//JMenu solvateMoleculeSubmenu;

////////////


     JMenu displayMoleculeSubmenu;
     JMenu displayDrugSubmenu, displayAminoAcidSubmenu, displayLipidSubmenu, displayNucleicAcidSubmenu, displaySugarSubmenu, displayCofactorSubmenu, displayVitaminSubmenu;
     JMenu displayStyleSubmenu, projectionSubmenu;
     JMenu editMoleculeSubmenu;
     JMenu hybridizationMenu, hybridizationSubmenu;

     // Define menu items
     JMenuItem openItem, saveItem, saveAsItem, printItem, closeItem;
     JMenuItem openPopupItem, savePopupItem, saveAsPopupItem, importPopupItem, exportPopupItem, printPopupItem, closePopupItem;
     JMenuItem undoItem, redoItem, cutItem, copyItem, pasteItem, clearItem, addItem;
     JMenuItem lineItem, ball_and_stickItem, orthographicItem, perspectiveItem, propertiesItem;
     JMenuItem showBondsItem, hideBondsItem, colorBondsItem, uncolorBondsItem, adjustBondCriteriaItem;
     JMenuItem displayItem, displayOption1, displayOption2, displayOption3, displayOption4, displayOption5, displayOption6, displayOption7, displayOption8;
     JMenuItem gaussianImportItem, gamessImportItem, gaussianExportItem, gamessExportItem;
     JMenuItem importItem, exportItem, importOption1, importOption2, exportOption1, exportOption2; 
     JMenuItem drawItem, buildMoleculeItem, createMoleculeItem, buildNanostructureItem, buildPolymerItem, buildCrystalItem;
     JMenuItem addAtomBuildItem, addFcnalGrpBuildItem;
     JMenuItem createNanotubeItem, createNanowireItem, createNanoclusterItem;
     JMenuItem displayAtomTypeTableItem, displayBondParametersTableItem, displayDihedralParametersTableItem, displayMoleculeItem;


JMenuItem generateAtomTypesItem_02, dihedralOptimizationItem;

     // Hybridization types
     JMenuItem spItem, sp2Item, sp3Item, trigonalPlanarItem, trigonalBipyramidalItem, octahedralItem, squarePlanarItem;

     // Functional groups
     JMenuItem aminoItem, benzothiazolylItem, carboxylItem, esterItem, formylItem, hydroxylItem, ketoneItem, methylItem, phenylItem, sulfhydrylItem;

     // Fragments
     JMenuItem ammoniaItem, benzeneItem, benzothiazole2Item, formicAcidItem, formaldehydeItem, furanItem, hydrogenSulfideItem, methaneItem;
     JMenuItem phosphateItem, pyridineItem, pyrroleItem, thiazoleItem, thiopheneItem, waterItem;

////////////
//
//  GUI:

JMenuItem solvateItem, addH2OItem;


// Class 1 additive FFs
JMenuItem amberItem, charmmItem, cvffItem, eceppItem, gromacsItem, gromosItem, oplsItem, saapItem;
JMenuItem allAtomItem, unitedAtomItem, cgenffItem;
JMenuItem oplsaaItem, oplsuaItem, opls2001Item, opls2005Item;

// Class 2 additive FFs
JMenuItem cffItem, compassItem, dreidingItem, mm3Item, mm4Item, mmffItem, uffItem;

// Polarizable FFs
JMenuItem amberpolItem, amoebaItem, cosmosItem, drfItem, pffItem, sibfaItem, tinkerItem, xpolItem;
// Polarizable CHARMM FFs
JMenuItem cheqItem, drudeItem, pipfItem;

// Reactive FFs
JMenuItem reaxffItem, evbItem, rwffItem;

// Coarse-grained FFs
JMenuItem martiniItem, unresItem;

// Many-body FFs

// Special FFs
JMenuItem cosmosnmrItem, CuClusterItem, goipItem;

// Other FFs
JMenuItem valbondItem;

////////////


     // Amino acids
     JMenuItem alanineItem, arginineItem, asparagineItem, aspartateItem, cysteineItem, glutamateItem, glutamineItem, glycineItem, histidineItem, isoleucineItem;
     JMenuItem leucineItem, lysineItem, methionineItem, phenylalanineItem, prolineItem, serineItem, threonineItem, tryptophanItem, tyrosineItem, valineItem; 

     // Drugs
     JMenuItem ampicillinItem, benzothiazoleItem, cephalosporinCItem, caffeineItem, chloramphenicolItem, cinnamideItem, epinephrineItem, erythromycinAItem; 
     JMenuItem kanamycinAItem, kanamycinCItem, morphineItem, neomycinItem, penicillinGItem, penicillinVItem, riluzoleItem, streptinomycinItem, streptomycinItem;

     // Carbohydrates
     JMenuItem aspartameItem, alphaGalactoseItem, betaGalactoseItem, alphaGlucoseItem, betaGlucoseItem, deoxyglucoseItem, fructoseItem, glucuronicAcidItem;
     JMenuItem alphaLactoseItem, betaLactoseItem, mannoseItem, NAGItem, alphaRhamnoseItem, riboseItem, deoxyriboseItem, trehaloseItem, xyloseItem;

     // Cofactors
     JMenuItem FADItem, FMNItem, NADPHItem;

     // Lipids
     JMenuItem arachidonicAcidItem, isopreneItem;

     // Nucleic acids
     JMenuItem ADPItem, AMPItem, GTPItem, cyclicAMPItem;

     // Vitamins
     JMenuItem ascorbicAcidItem, biotinItem, folicAcidItem, nicotinicAcidItem, pantothenicAcidItem, pyridoxalPItem, riboflavinItem, thiaminePPItem, tocopherolItem, vitaminD3Item;

     JMenuItem editItem, modifyGeometryItem, joinFragmentsItem, addHItem, replaceAtomItem, modifyAtomTypeItem, modifyChargeItem;
     JMenuItem chargeDensityItem, homoItem, lumoItem, electrostaticPotenialItem;

     // Define popup menus
     JPopupMenu filePopupMenu, editPopupMenu, viewPopupMenu, exportPopupMenu, importPopupMenu, helpPopupMenu;
     JPopupMenu structurePopupMenu, forceFieldPopupMenu, runPopupMenu, displayPopupMenu, settingsPopupMenu;
     JPopupMenu importPopupSubmenu, exportPopupSubmenu, displayOptionsPopupSubmenu, showBondsPopupSubmenu, displayMOPopupSubmenu, animationPopupSubmenu, outputdataPopupSubmenu;
     JPopupMenu displayStylePopupSubmenu, projectionPopupSubmenu;
     JPopupMenu editMoleculePopupSubmenu;
     JPopupMenu buildMoleculePopupSubmenu, createMoleculePopupSubmenu;
     JPopupMenu sketchFcnalGrpPopupSubmenu, drawFcnalGrpPopupSubmenu, drawFragmentPopupSubmenu;
     JPopupMenu sketchAcidPopupSubmenu, sketchAlcoholPopupSubmenu, sketchAldehydePopupSubmenu;
     JPopupMenu drawAcidPopupSubmenu, drawAlcoholPopupSubmenu, drawAldehydePopupSubmenu;
     JPopupMenu createNanostructurePopupSubmenu;
     JPopupMenu displayStructuresPopupMenu, displayMoleculePopupSubmenu, displayAminoAcidPopupSubmenu; 
     JPopupMenu displayDrugPopupSubmenu, displayLipidPopupSubmenu, displayNucleicAcidPopupSubmenu;
     JPopupMenu displaySugarPopupSubmenu, displayCofactorPopupSubmenu, displayVitaminPopupSubmenu;
     JPopupMenu hybridizationPopupMenu,hybridizationPopupSubmenu;

////////////
//
//  GUI:

JPopupMenu empiricalPopupMenu, empiricalPopupSubmenu, semiempiricalPopupMenu, semiempiricalPopupSubmenu, coarseGrainedPopupMenu, coarseGrainedPopupSubmenu;
JPopupMenu manyBodyPopupMenu, manyBodyPopupSubmenu, specialFFPopupMenu, specialFFPopupSubmenu, userDefinedPopupMenu, userDefinedPopupSubmenu;

JPopupMenu class1additivePopupMenu, class1additivePopupSubmenu, class2additivePopupMenu, class2additivePopupSubmenu; 
JPopupMenu charmmPopupSubmenu, oplsPopupSubmenu;
JPopupMenu polarizableFFPopupMenu, polarizableFFPopupSubmenu, charmmpolFFPopupMenu, charmmpolFFPopupSubmenu, reactiveFFPopupMenu; 
JPopupMenu reactiveFFPopupSubmenu, otherFFPopupMenu, otherFFPopupSubmenu;


JFrame paramFrame, dhOptFrame;
JButton bAdd, bEdit, bCancel, bSave;


JPopupMenu parametersPopupMenu;
JMenu paramSubmenu;
JMenuItem generateAtomTypesItem, assignParamValueItem, viewParamItem, searchDBItem;

JComboBox cmbSelectedBonds = new JComboBox();
JTextField tfKb, tfR0, tfAtomTypes;
JTextField dhAtomsTxt, dhAtmTypTxt, svTxt, evTxt, stpsizTxt;

JPanel pBondParams1, pBondParams2, pSelectedBond, pAtomTypes, pButtons, dhOptPane;
JLabel lbKb, lbR0,lbBondID, lbAtomTypes;  
JLabel dhLabel, dhAtmTypLabel, svLabel, evLabel, stpsizLabel, dhSelection1Label, dhSelection2Label;

JRadioButton dhSelection1Rb, dhSelection2Rb;
ButtonGroup dhBtnGrp;
JButton dhSubmitBtn, dhCancelBtn;

ArrayList<Integer> alDihedralAtoms;

ArrayList<Integer> rowndxList;
ArrayList<Integer> selectedRowNdxList;
ArrayList<Integer> dhCentralBondIndices;
ArrayList<Integer> minDHCentralBondIndices;
ArrayList<Integer> maxDHCentralBondIndices;

ArrayList<String> selectedDihedrals;


String[] arrDHAtomTypes;

////////////



     JLabel editModeLabel, solvateMoleculeLabel;

     JComboBox elementList, fragmentList;

JCheckBox jaychkbox;

     Container container;

     TitledBorder border;

     JPanel coordsPanel, iconPanel, iconPanel2, elementSelectionPanel;
     FlowLayout coordsLayout, iconLayout, iconLayout2, elementSelectionLayout;
     JLabel elemLabel, bondLabel, dihedralBondLabel, bondLengthLabel, atomTypeLabel, chargeLabel, xLabel, yLabel, zLabel, bondAngleLabel, dihedralAngleLabel, iconLabel1, iconLabel2;
     JTextField elemField, bondField, bondLengthField, atomTypeField, chargeField, xCoordField, yCoordField, zCoordField, bondAngleField, dihedralBondField, dihedralAngleField;

     String[] selectableElements = {"  H", "  C", "  O", "  N", "  P", "  S", "  F", "  Cl", "  Br", "  I", "  He", "  Li", "  Be", "  B", "  Ne",
                                    "  He", "  Li", "  Be", "  B", "  Ne", "  Na", "  Mg", "  Al", "  Si", "  Ar", "  K", "  Ca", "  Sc", "  Ti", 
                                    "  V", "  Cr", "  Mn", " Fe", "  Co", "  Ni", "  Cu", "  Zn", "  Ga", "  Ge", "  As", "  Se", "  Kr", "  Rb", 
                                    "  Sr", "  Y", "  Zr", "  Nb", "  Mo", "  Tc", "  Ru", "  Rh", "  Pd", "  Ag", "  Cd", "  In", "  Sn", "  Sb", 
                                    "  Te", "  Xe", "  Cs", "  Ba", "  La", "  Ce", "  Pr", "  Nd", "  Pm", "  Sm", "  Eu", "  Gd", "  Tb", "  Dy",
                                    "  Ho", "  Er", "  Tm", "  Yb", "  Lu", "  Hf", "  Ta", "  W", "  Re", "  Os", "  Ir", "  Pt", "  Au", "  Hg", 
                                    "  Tl", "  Pb", "  Bi", "  Po", "  At", "  Rn", "  Fr", "  Ra", "  Ac", "  Th", "  Pa", "  U", "  Np", "  Pu", 
                                    "  Am", "  Cm", "  Bk", "  Cf", "  Es", "  Fm", "  Md", "  No", "  Lr", "  Rf", "  Db", "  Sg", "  Bh", "  Hs",
                                    "  Mt", "  Ds", "  Rg"};
                  

     int dhAtmTypCellWidth, dhAngleCellWidth;
     String[] tableColNames = {"Atom Name", "Atom Types"};
     Object[][] tableContents = new Object[2][2];  

     // JTables for atom types, bond angles, and dihedral angles
     JTable atomTypeTable, bondAnglesTable, dhParametersTable;

     DefaultTableModel atmTypTableModel, bndAngleTableModel, dhAngleTableModel;
     DefaultTableCellRenderer tableCellRenderer;
     JTableHeader tblheader;
     TableColumn tblcol;
     JLabel tblCell;
     String dhToolTipText;

     String[] colnames = {"", "Dihedral Parameter", "Penalty"};
     String[][] tabldata = { {"", "", ""} };

     // Scroll panes for atom types, bond angles, and dihedral angles
     JScrollPane atypTableScrollPane, bndAnglTableScrollPane, dhAnglTableScrollPane, dhAtmTypeTxtAreaScrollPane;  

     // JFrames for atom types, bond angles, and dihedral angles
     JFrame atomTypeTableFrame, bondAnglesTableFrame, dhParametersTableFrame;


     // Create a file chooser
     JFileChooser fc = new JFileChooser();

     // Specify structure of water (including lone pair electrons) used to solvate a molecule
     double[][] solventH2OCoordinates = { {6.0, 0.0, 0.0, 0.0}, {1, -0.029140, 0.904399, 0.312339}, {1, 0.933643, -0.200520, -0.066830} };
     double[][] LPs = { {-0.435840, -0.608240, 0.663390}, {-0.459750, -0.085800, -0.883890} };
     int[][] solventH2OConnectivity = { {0, 1, 1}, {1, 0, 0}, {1, 0, 0} };

	private JMenuItem xyplotMenuItem;


     public Nanocad3D()
     {
          super("ParamChem");

          //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

          // Create display
//          NanocadDisplay mdisplay = new NanocadDisplay();
          ParamcadDisplay mdisplay = new ParamcadDisplay();

          GLCapabilities glcaps = new GLCapabilities();
          glcaps.setDoubleBuffered(true);

          glcanvas = GLDrawableFactory.getFactory().createGLCanvas(glcaps);
          glcanvas.addGLEventListener(mdisplay);
          glcanvas.addMouseMotionListener(mdisplay);
          glcanvas.addMouseListener(new CanvasMouseListener());
          glcanvas.addKeyListener(new CanvasKeyListener());
          getContentPane().add(glcanvas, BorderLayout.CENTER); 

          // Set initial canvas dimension
          canvasHeight = 600; canvasWidth = 850;
          setSize(canvasWidth, canvasHeight);

          minCanvasDimen = Math.min(canvasHeight, canvasWidth);

          centerWindow(this);

          ImageIcon icon = new ImageIcon("centerMolecule.gif");

          elemLabel = new JLabel(" Atom ID:");
          elemLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          bondLabel = new JLabel(" Bond ID:");
          bondLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          bondLengthLabel = new JLabel(" Bond length:");
          bondLengthLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          dihedralBondLabel = new JLabel(" Dihedral atoms:");
          dihedralBondLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          atomTypeLabel = new JLabel(" Atom type:");
          atomTypeLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          chargeLabel = new JLabel(" Charge:");
          chargeLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          xLabel = new JLabel(" x:");  xLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          yLabel = new JLabel(" y:");  yLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          zLabel = new JLabel(" z:");  zLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          bondAngleLabel = new JLabel("Bond angle:");  bondAngleLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
          dihedralAngleLabel = new JLabel("Dihedral angle:");  dihedralAngleLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));

          elemField = new JTextField(6);          elemField.setFont(new Font("Lucinda Console", Font.BOLD, 12));            elemField.setHorizontalAlignment(JTextField.CENTER);
          bondField = new JTextField(10);         bondField.setFont(new Font("Lucinda Console", Font.BOLD, 12));            bondField.setHorizontalAlignment(JTextField.CENTER);
          bondLengthField = new JTextField(7);    bondLengthField.setFont(new Font("Lucinda Console", Font.BOLD, 12));      bondLengthField.setHorizontalAlignment(JTextField.CENTER);
          bondLengthField.addMouseListener(new textFieldListener());
          atomTypeField = new JTextField(8);      atomTypeField.setFont(new Font("Arial", Font.BOLD, 12));                  atomTypeField.setHorizontalAlignment(JTextField.CENTER);
          chargeField = new JTextField(6);        chargeField.setFont(new Font("Arial", Font.BOLD, 12));                    chargeField.setHorizontalAlignment(JTextField.CENTER);
          xCoordField = new JTextField(7);        xCoordField.setFont(new Font("Lucinda Console", Font.BOLD, 12));          xCoordField.setHorizontalAlignment(JTextField.CENTER);
          xCoordField.addMouseListener(new textFieldListener());
          yCoordField = new JTextField(7);        yCoordField.setFont(new Font("Lucinda Console", Font.BOLD, 12));          yCoordField.setHorizontalAlignment(JTextField.CENTER);
          yCoordField.addMouseListener(new textFieldListener());
          zCoordField = new JTextField(7);        zCoordField.setFont(new Font("Lucinda Console", Font.BOLD, 12));          zCoordField.setHorizontalAlignment(JTextField.CENTER);
          zCoordField.addMouseListener(new textFieldListener());
          bondAngleField = new JTextField(7);     bondAngleField.setFont(new Font("Lucinda Console", Font.BOLD, 12));       bondAngleField.setHorizontalAlignment(JTextField.CENTER);
          bondAngleField.addMouseListener(new textFieldListener());
          dihedralBondField = new JTextField(12);  dihedralBondField.setFont(new Font("Lucinda Console", Font.BOLD, 12));    dihedralBondField.setHorizontalAlignment(JTextField.CENTER);
          dihedralAngleField = new JTextField(7); dihedralAngleField.setFont(new Font("Lucinda Console", Font.BOLD, 12));  dihedralAngleField.setHorizontalAlignment(JTextField.CENTER);
          dihedralAngleField.addMouseListener(new textFieldListener());

          coordsPanel = new JPanel();
          coordsPanel.setPreferredSize(new Dimension(canvasWidth, (int)(0.04*canvasHeight)));
          coordsLayout = new FlowLayout(FlowLayout.LEFT, 5, 2); 
          coordsPanel.setLayout(coordsLayout);
          coordsPanel.add(elemLabel);  coordsPanel.add(elemField);
          coordsPanel.add(bondLabel);  coordsPanel.add(bondField);
          coordsPanel.add(bondLengthLabel);  coordsPanel.add(bondLengthField);
          coordsPanel.add(atomTypeLabel);  coordsPanel.add(atomTypeField);
          coordsPanel.add(chargeLabel);  coordsPanel.add(chargeField);
          coordsPanel.add(xLabel);  coordsPanel.add(xCoordField);
          coordsPanel.add(yLabel);  coordsPanel.add(yCoordField);
          coordsPanel.add(zLabel);  coordsPanel.add(zCoordField);
          coordsPanel.add(bondAngleLabel);  coordsPanel.add(bondAngleField);
          coordsPanel.add(dihedralBondLabel);  coordsPanel.add(dihedralBondField);
          coordsPanel.add(dihedralAngleLabel);  coordsPanel.add(dihedralAngleField);
          
          getContentPane().add(coordsPanel, BorderLayout.SOUTH); 


dhParametersTableFrame = new JFrame("Dihedral Parameters");

dhAngleTableModel = new DefaultTableModel(tabldata, colnames);
ListSelectionModel listSelectnMdl;

dhParametersTable = new JTable(dhAngleTableModel);

dhParametersTable.setRowSelectionAllowed(true);
dhParametersTable.setColumnSelectionAllowed(true);
dhParametersTable.setCellSelectionEnabled(true);
listSelectnMdl = dhParametersTable.getSelectionModel();
listSelectnMdl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//listSelectnMdl.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


dhAnglTableScrollPane = new JScrollPane(dhParametersTable);

//dhParametersTable.setPreferredScrollableViewportSize(new Dimension(500, 275));

dhParametersTableFrame.getContentPane().add(dhAnglTableScrollPane, BorderLayout.CENTER);

dhAnglTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 

dhParametersTable.addMouseListener(new dhParametersTableListener());


tableCellRenderer = new DefaultTableCellRenderer();
tblheader =  dhParametersTable.getTableHeader();
tblheader.setFont(new Font("Arial", Font.BOLD, 12));
//tblheader.setBackground(Color.cyan);
tblheader.setBackground(new Color(0, 0, 153));
tblheader.setForeground(new Color(255, 255, 255));
tableCellRenderer.setHorizontalTextPosition(DefaultTableCellRenderer.CENTER); 
tableCellRenderer.setFont(new Font("Arial", Font.BOLD, 12));


// 1st column of dhParametersTable
//tblcol = dhParametersTable.getColumnModel().getColumn(0);
tblcol = dhParametersTable.getColumnModel().getColumn(1);
ToolTipManager.sharedInstance().setInitialDelay(0);
tblcol.setCellRenderer(tableCellRenderer);

listSelectnMdl.addListSelectionListener(new dhListSelectionListener());

// 2nd column of dhParametersTable
tblcol = dhParametersTable.getColumnModel().getColumn(1);
tblcol.setCellRenderer(new jtableCellRenderer());
//tableCellRenderer.setToolTipText(getDhToolTip());
//tblcol.setCellRenderer(tableCellRenderer);
tblcol.setCellRenderer(new jtableCellRenderer());
//tableCellRenderer.setToolTipText(getDhToolTip());

// 3rd column of dhParametersTable
tblcol = dhParametersTable.getColumnModel().getColumn(2);
tblcol.setCellRenderer(new jtableCellRenderer());

dhParametersTable.getColumnModel().getColumn(0).setPreferredWidth(5);
dhParametersTable.getColumnModel().getColumn(1).setPreferredWidth(150);
dhParametersTable.getColumnModel().getColumn(1).setPreferredWidth(150);

//dhParametersTable.setCellSelectionEnabled(true);

dhParametersTableFrame.setSize(325, 275);
dhParametersTableFrame.setLocation(350, 350);
dhParametersTableFrame.setAlwaysOnTop(true);

// Set default close operation to close only the dihedral angles table
//dhParametersTableFrame.setDefaultCloseOperation(0);    // DO_NOTHING_ON_CLOSE
dhParametersTableFrame.setDefaultCloseOperation(1);      // HIDE_ON_CLOSE
//dhParametersTableFrame.setDefaultCloseOperation(2);    // DISPOSE_ON_CLOS
//dhParametersTableFrame.setDefaultCloseOperation(3);    // EXIT_ON_CLOSE

//dhParametersTableFrame.setVisible(false);
dhParametersTableFrame.setVisible(false);


          // Hide all items in coordsPanel initially
          elemLabel.setVisible(false);          elemField.setVisible(false);
          atomTypeLabel.setVisible(false);      atomTypeField.setVisible(false); 
          chargeLabel.setVisible(false);        chargeField.setVisible(false);

          bondLabel.setVisible(false);          bondField.setVisible(false); 
          bondLengthLabel.setVisible(false);    bondLengthField.setVisible(false);
          xLabel.setVisible(false);             xCoordField.setVisible(false);
          yLabel.setVisible(false);             yCoordField.setVisible(false);
          zLabel.setVisible(false);             zCoordField.setVisible(false);                    
          bondAngleLabel.setVisible(false);     bondAngleField.setVisible(false);
          dihedralBondLabel.setVisible(false);  dihedralBondField.setVisible(false); 
          dihedralAngleLabel.setVisible(false); dihedralAngleField.setVisible(false);

          elementList = new JComboBox(selectableElements);
          elementList.setEnabled(true);
          elementList.setEditable(false);
          elementList.setSelectedIndex(1);
          elementList.addActionListener(new elemListActionListener());
          elementList.setPreferredSize(new Dimension(75, 20));
          elementList.setLayout(elementSelectionLayout);
          elementList.setLightWeightPopupEnabled(false);

          elementSelectionPanel = new JPanel();
          elementSelectionPanel.setBackground(Color.black);
          elementSelectionPanel.setPreferredSize(new Dimension(0, 25));
          elementSelectionLayout = new FlowLayout(FlowLayout.LEFT, 65, 3);
          elementSelectionPanel.setLayout(elementSelectionLayout);
          elementSelectionPanel.add(elementList);

          editModeLabel = new JLabel("EDIT Mode");
          editModeLabel.setForeground(Color.red);
          elementSelectionPanel.add(editModeLabel);
          editModeLabel.setVisible(false);

          getContentPane().add(elementSelectionPanel, BorderLayout.NORTH);
          elementSelectionPanel.setVisible(true);
          elementList.setVisible(false);

          solvateMoleculeLabel = new JLabel("Solvate Molecule");
          solvateMoleculeLabel.setForeground(Color.yellow);
          elementSelectionPanel.add(solvateMoleculeLabel);
          solvateMoleculeLabel.setVisible(false);

          getContentPane().add(elementSelectionPanel, BorderLayout.NORTH);
          elementSelectionPanel.setVisible(true);
          elementList.setVisible(false);

          // Create menus
          menuBar = new JMenuBar();
          setJMenuBar(menuBar);

          // Create menus on main menu bar
          fileMenu = new JMenu("File");
          fileMenu.addMouseListener(new menuMouseListener());
          editMenu = new JMenu("Edit");
          editMenu.addMouseListener(new menuMouseListener());
          viewMenu = new JMenu("View");
          viewMenu.addMouseListener(new menuMouseListener());
          structureMenu = new JMenu("Structure");
          structureMenu.addMouseListener(new menuMouseListener());          
          forceFieldMenu = new JMenu("Force Field");
          forceFieldMenu.addMouseListener(new menuMouseListener());          
          parametersMenu = new JMenu("Parameters");
          parametersMenu.addMouseListener(new menuMouseListener());          

          runMenu = new JMenu("Run");
          runMenu.addMouseListener(new menuMouseListener());
          displayMenu = new JMenu("Display");
          displayMenu.addMouseListener(new menuMouseListener());
          settingsMenu = new JMenu("Settings");
          settingsMenu.addMouseListener(new menuMouseListener());
          helpMenu = new JMenu("Help");
          helpMenu.addMouseListener(new menuMouseListener());

          // Create 'File' popup menu associated with 'File' menu
          filePopupMenu = new JPopupMenu(); 
               openItem = new JMenuItem("Open");
               openItem.addMouseListener(new fileSubmenuMouseListener());
               filePopupMenu.add(openItem);          
               filePopupMenu.addSeparator();
               saveItem = new JMenuItem("Save");
               filePopupMenu.add(saveItem);
               saveAsItem = new JMenuItem("SaveAs");
               saveAsItem.addMouseListener(new fileSubmenuMouseListener());
               filePopupMenu.add(saveAsItem);
               filePopupMenu.addSeparator();

          // Create 'Import' submenu
          importSubmenu = new JMenu("Import");
          importSubmenu.addMouseListener(new importSubmenuMouseListener());
          importPopupSubmenu = new JPopupMenu();
          importPopupSubmenu.addMouseListener(new importSubmenuMouseListener());
               // Add submenu items
               gaussianImportItem = new JMenuItem("Gaussian");
               gaussianImportItem.addMouseListener(new gaussianImportMouseListener());
               importPopupSubmenu.add(gaussianImportItem);
               gamessImportItem = new JMenuItem("Gamess");
               gamessImportItem.addMouseListener(new gamessImportMouseListener());
               importPopupSubmenu.add(gamessImportItem);
          importPopupSubmenu.setLightWeightPopupEnabled(false);
          importPopupSubmenu.setVisible(false);    

          filePopupMenu.add(importSubmenu);

          // Create 'Export' submenu
          exportSubmenu = new JMenu("Export");
          exportSubmenu.addMouseListener(new exportSubmenuMouseListener());
          exportPopupSubmenu = new JPopupMenu();
          exportPopupSubmenu.addMouseListener(new exportSubmenuMouseListener());
               // Add submenu items
               gaussianExportItem = new JMenuItem("Gaussian");
               gaussianExportItem.addMouseListener(new gaussianExportMouseListener());
               exportPopupSubmenu.add(gaussianExportItem);
               gamessExportItem = new JMenuItem("Gamess");
               gamessExportItem.addMouseListener(new gamessExportMouseListener());
               exportPopupSubmenu.add(gamessExportItem);
          exportPopupSubmenu.setLightWeightPopupEnabled(false);
          exportPopupSubmenu.setVisible(false);    

          filePopupMenu.add(exportSubmenu);

          printItem = new JMenuItem("Print");

          closeItem = new JMenuItem("Close");
          filePopupMenu.add(closeItem);
          filePopupMenu.setLightWeightPopupEnabled(false);
          filePopupMenu.setVisible(false);    
          menuBar.add(fileMenu);
 
          // Create edit popup menu associated with Edit menu
          editPopupMenu = new JPopupMenu();
          undoItem = new JMenuItem("Undo");
          undoItem.addMouseListener(new editSubmenuMouseListener());
          editPopupMenu.add(undoItem);          
          redoItem = new JMenuItem("Redo");
          redoItem.addMouseListener(new editSubmenuMouseListener());
          editPopupMenu.add(redoItem);
          editPopupMenu.addSeparator();
          cutItem = new JMenuItem("Cut");
          editPopupMenu.add(cutItem);
          copyItem = new JMenuItem("Copy");
          editPopupMenu.add(copyItem);

          pasteItem = new JMenuItem("Paste");
          pasteItem.addMouseListener(new editSubmenuMouseListener());
          editPopupMenu.add(pasteItem);

          editPopupMenu.add(pasteItem);
          editPopupMenu.addSeparator();
          clearItem = new JMenuItem("Clear");
          editPopupMenu.add(clearItem);
          editPopupMenu.setLightWeightPopupEnabled(false);
          editPopupMenu.setVisible(false);    
          menuBar.add(editMenu);

          // Create 'View' popup menu associated with 'View' menu
          viewPopupMenu = new JPopupMenu();
               // Create Projection submenu
               displayStyleSubmenu = new JMenu("Display style");
               displayStyleSubmenu.addMouseListener(new viewSubmenuMouseListener());
               displayStylePopupSubmenu = new JPopupMenu();
               displayStylePopupSubmenu.addMouseListener(new viewSubmenuMouseListener());
                    // Add popup submenu items
                    lineItem = new JMenuItem("Line");
                    lineItem.addMouseListener(new displayStylePopupSubmenuMouseListener());
                    displayStylePopupSubmenu.add(lineItem);
                    ball_and_stickItem = new JMenuItem("Ball and stick");
                    ball_and_stickItem.addMouseListener(new displayStylePopupSubmenuMouseListener());
                    displayStylePopupSubmenu.add(ball_and_stickItem);
               displayStylePopupSubmenu.setLightWeightPopupEnabled(false);
               displayStylePopupSubmenu.setVisible(false);
               viewPopupMenu.add(displayStyleSubmenu);
               projectionSubmenu = new JMenu("Projection Type");
               projectionSubmenu.addMouseListener(new viewSubmenuMouseListener());
               projectionPopupSubmenu = new JPopupMenu();
               projectionPopupSubmenu.addMouseListener(new projectionSubmenuMouseListener());
                    // Add popup submenu items
                    orthographicItem = new JMenuItem("Orthographic View");
                    orthographicItem.addMouseListener(new viewPopupSubmenuMouseListener());
                    projectionPopupSubmenu.add(orthographicItem);
                    perspectiveItem = new JMenuItem("Perspective View");
                    perspectiveItem.addMouseListener(new viewPopupSubmenuMouseListener());
                    projectionPopupSubmenu.add(perspectiveItem);
               projectionPopupSubmenu.setLightWeightPopupEnabled(false);
               projectionPopupSubmenu.setVisible(false);    
               viewPopupMenu.add(projectionSubmenu);

          propertiesItem = new JMenuItem("Properties");
          viewPopupMenu.add(propertiesItem);
          viewPopupMenu.setLightWeightPopupEnabled(false);
          viewPopupMenu.setVisible(false);   
          menuBar.add(viewMenu);

          // Create Structute popup menu associated with Structure menu
          structurePopupMenu = new JPopupMenu();

          drawItem = new JMenuItem("Draw Molecule:");
          structurePopupMenu.add(drawItem);          

          // Create 'Draw Molecule' submenu
          createMoleculeSubmenu = new JMenu("    Molecule");
          createMoleculeSubmenu.addMouseListener(new createMoleculeSubmenuMouseListener());
          createMoleculePopupSubmenu = new JPopupMenu();
          createMoleculePopupSubmenu.addMouseListener(new createMoleculeSubmenuMouseListener());

               newStuctureItem = new JMenuItem("    New Molecule");
               newStuctureItem.addMouseListener(new createMoleculeSubmenuMouseListener());
               structurePopupMenu.add(newStuctureItem);

               structurePopupMenu.addSeparator();

               drawAtomItem = new JMenuItem("    Draw Atom");
               drawAtomItem.addMouseListener(new drawAtomMouseListener());
               structurePopupMenu.add(drawAtomItem);

               structureMenuItems.add(drawAtomItem);
               structureMenuItems.get(0).setEnabled(false);      // Disable 'Draw Atom' menu item

               drawFcnalGrpItem = new JMenuItem("    Draw Functional Group");
               drawFcnalGrpItem.addMouseListener(new drawFcnalGrpMouseListener());

               drawFcnalGrpPopupSubmenu = new JPopupMenu();

               structurePopupMenu.add(drawFcnalGrpItem);

               structureMenuItems.add(drawFcnalGrpItem);
               structureMenuItems.get(1).setEnabled(false);      // Disable 'Draw Functional Group' menu item


               drawFragmentItem = new JMenuItem("    Draw Fragment");
               drawFragmentItem.addMouseListener(new drawFragmentMouseListener());

               drawFragmentSubmenu = new JMenu("    Draw Fragment");

               drawFragmentPopupSubmenu = new JPopupMenu();

               structurePopupMenu.add(drawFragmentItem);

               structureMenuItems.add(drawFragmentItem);
               structureMenuItems.get(2).setEnabled(false);      // Disable 'Draw Fragment' menu item

// drawFragmentSubmenu.setEnabled(false);

               // Create 'Nanostructure' submenu
               createNanostructureSubmenu = new JMenu("    Nanostructure");
               createNanostructureSubmenu.addMouseListener(new createNanostructureSubmenuMouseListener());
               createNanostructurePopupSubmenu = new JPopupMenu();
               createNanostructurePopupSubmenu.addMouseListener(new createNanostructureSubmenuMouseListener()); 
                    // Add popup submenu items
                    createNanotubeItem = new JMenuItem("Nanotube");
//                    createNanotubeItem.addMouseListener(new nanotubeMouseListener());
                    createNanostructurePopupSubmenu.add(createNanotubeItem);
                    createNanowireItem = new JMenuItem("Nanowire");
//                    createNanowireItem.addMouseListener(new nanowireMouseListener());
                    createNanostructurePopupSubmenu.add(createNanowireItem);
                    createNanoclusterItem = new JMenuItem("Nanocluster");
//                    createNanoclusterItem.addMouseListener(new nanoclusterMouseListener());
                    createNanostructurePopupSubmenu.add(createNanoclusterItem);
               createNanostructurePopupSubmenu.setLightWeightPopupEnabled(false);
               createNanostructurePopupSubmenu.setVisible(false);    
               structurePopupMenu.add(createNanostructureSubmenu);

//structureSubmenus.add(createNanostructureSubmenu);
//structureSubmenus.get(3).setEnabled(false);

          buildPolymerItem = new JMenuItem("    Polymer");
          structurePopupMenu.add(buildPolymerItem);

          buildCrystalItem = new JMenuItem("    Crystal");
          structurePopupMenu.add(buildCrystalItem);

          structurePopupMenu.addSeparator();

          editItem = new JMenuItem("Edit Molecule:");
          structurePopupMenu.add(editItem);          

          // Create 'Edit Molecule' submenu
          editMoleculeSubmenu = new JMenu("    Molecule");
          editMoleculeSubmenu.addMouseListener(new editMoleculeSubmenuMouseListener());
          editMoleculePopupSubmenu = new JPopupMenu();
          editMoleculePopupSubmenu.addMouseListener(new editMoleculeSubmenuMouseListener());
          // Add popup submenu items
               modifyGeometryItem = new JMenuItem("    Modify geometry");
               modifyGeometryItem.addMouseListener(new editMoleculeSubmenuMouseListener());
               structurePopupMenu.add(modifyGeometryItem); 
               addHItem = new JMenuItem("    Add hydrogens");
               addHItem.addMouseListener(new editMoleculeSubmenuMouseListener());
               structurePopupMenu.add(addHItem);  
               joinFragmentsItem = new JMenuItem("    Join fragments");
               joinFragmentsItem.addMouseListener(new editMoleculeSubmenuMouseListener());
               structurePopupMenu.add(joinFragmentsItem);   
               replaceAtomItem = new JMenuItem("    Replace atom");
               replaceAtomItem.addMouseListener(new editMoleculeSubmenuMouseListener());

               // Create hybridization submenu
               hybridizationSubmenu = new JMenu("    Modify hybridization");
               hybridizationSubmenu.addMouseListener(new hybridizationSubmenuMouseListener());
               hybridizationPopupSubmenu = new JPopupMenu();
               // Add popup submenu items
                    sp3Item = new JMenuItem("sp3 (tetrahedral)");
                    sp3Item.addMouseListener(new hybridizationPopupSubmenuMouseListener());
                    hybridizationPopupSubmenu.add(sp3Item);
                    sp2Item = new JMenuItem("sp2 (trigonal)");
                    sp2Item.addMouseListener(new hybridizationPopupSubmenuMouseListener());
                    hybridizationPopupSubmenu.add(sp2Item);
                    spItem = new JMenuItem("sp (linear)");
                    spItem.addMouseListener(new hybridizationPopupSubmenuMouseListener());
                    hybridizationPopupSubmenu.add(spItem);
                    trigonalPlanarItem = new JMenuItem("trigonal planar");
                    trigonalPlanarItem.addMouseListener(new hybridizationPopupSubmenuMouseListener());
                    hybridizationPopupSubmenu.add(trigonalPlanarItem);
                    trigonalBipyramidalItem = new JMenuItem("trigonal bipyramidal (sp3d)");
                    trigonalBipyramidalItem.addMouseListener(new hybridizationPopupSubmenuMouseListener());
                    hybridizationPopupSubmenu.add(trigonalBipyramidalItem);
                    octahedralItem = new JMenuItem("octahedral");
                    octahedralItem.addMouseListener(new hybridizationPopupSubmenuMouseListener());
                    hybridizationPopupSubmenu.add(octahedralItem);
                    squarePlanarItem = new JMenuItem("square planar (dsp2)");
                    squarePlanarItem.addMouseListener(new hybridizationPopupSubmenuMouseListener());
                    hybridizationPopupSubmenu.add(squarePlanarItem);
               hybridizationPopupSubmenu.setLightWeightPopupEnabled(false);
               hybridizationPopupSubmenu.setVisible(false);  
               structurePopupMenu.add(hybridizationSubmenu);

          // Add remaining submenu items
               structurePopupMenu.add(replaceAtomItem);   
               modifyAtomTypeItem = new JMenuItem("    Modify atom type");
               modifyAtomTypeItem.addMouseListener(new editMoleculeSubmenuMouseListener());
               structurePopupMenu.add(modifyAtomTypeItem);   
               modifyChargeItem = new JMenuItem("    Modify charge");
               modifyChargeItem.addMouseListener(new editMoleculeSubmenuMouseListener());
               structurePopupMenu.add(modifyChargeItem);   

               structurePopupMenu.addSeparator();

               solvateItem = new JMenuItem("Solvate Molecule:");
               structurePopupMenu.add(solvateItem);          
          // Create Edit Molecule submenu
               addH2OItem = new JMenuItem("<html>&nbsp&nbsp&nbsp Add H<sub>2</sub>O molecule</html>");
               addH2OItem.addMouseListener(new editMoleculeSubmenuMouseListener());
               structurePopupMenu.add(addH2OItem);
          structurePopupMenu.setLightWeightPopupEnabled(false);
          structurePopupMenu.setVisible(false);  

          menuBar.add(structureMenu);

          displayPopupMenu = new JPopupMenu();

          displayAtomTypeTableItem = new JMenuItem("Atom types table"); 
          displayPopupMenu.add(displayAtomTypeTableItem);
          displayAtomTypeTableItem.setEnabled(false);

          displayBondParametersTableItem = new JMenuItem("Bond parameters table"); 
          displayPopupMenu.add(displayBondParametersTableItem);
          displayBondParametersTableItem.setEnabled(false);

          displayDihedralParametersTableItem = new JMenuItem("Dihedral parameters table"); 
/////
          displayDihedralParametersTableItem.addMouseListener(new paramTableListener());
/////
          displayPopupMenu.add(displayDihedralParametersTableItem);
          displayDihedralParametersTableItem.setEnabled(false);

          displayPopupMenu.addSeparator();

          displayMoleculeItem = new JMenuItem("Molecule:");
          displayPopupMenu.add(displayMoleculeItem);          
 
          // Create Display Molecule submenu
          displayAminoAcidSubmenu = new JMenu("      Amino Acids");
          displayAminoAcidSubmenu.addMouseListener(new displaySubmenuMouseListener());
          displayPopupMenu.add(displayAminoAcidSubmenu);

          displaySugarSubmenu = new JMenu("      Carbohydrates");
          displaySugarSubmenu.addMouseListener(new displaySubmenuMouseListener());
          displayPopupMenu.add(displaySugarSubmenu);

//          displayPopupMenu.setLightWeightPopupEnabled(false);
//          displayPopupMenu.setVisible(false);    

          displayCofactorSubmenu = new JMenu("      Cofactors");
          displayCofactorSubmenu.addMouseListener(new displaySubmenuMouseListener());
          displayCofactorPopupSubmenu = new JPopupMenu();
          displayCofactorPopupSubmenu.addMouseListener(new displaySubmenuMouseListener()); 
               // Add submenu items    
          FADItem = new JMenuItem("flavin adenine dinucleotide");
               displayCofactorPopupSubmenu.add(FADItem);
          FMNItem = new JMenuItem("flavin mononucleotide");
               displayCofactorPopupSubmenu.add(FMNItem);
          NADPHItem = new JMenuItem("NADPH");
               displayCofactorPopupSubmenu.add(NADPHItem);
          displayCofactorPopupSubmenu.setLightWeightPopupEnabled(false);
          displayCofactorPopupSubmenu.setVisible(false);
          displayPopupMenu.add(displayCofactorSubmenu);
          displayPopupMenu.setLightWeightPopupEnabled(false);
          displayPopupMenu.setVisible(false);    

          displayDrugSubmenu = new JMenu("      Drugs");
          displayDrugSubmenu.addMouseListener(new displaySubmenuMouseListener());
          displayDrugPopupSubmenu = new JPopupMenu();

//          displayDrugPopupSubmenu.addMouseListener(new displaySubmenuMouseListener()); 

          displayPopupMenu.add(displayDrugSubmenu);

          displayLipidSubmenu = new JMenu("      Lipids");
          displayLipidSubmenu.addMouseListener(new displaySubmenuMouseListener());
          displayLipidPopupSubmenu = new JPopupMenu();

//          displayLipidPopupSubmenu.addMouseListener(new displaySubmenuMouseListener()); 

          displayPopupMenu.add(displayLipidSubmenu);

          displayNucleicAcidSubmenu = new JMenu("      Nucleic Acids");
          displayNucleicAcidSubmenu.addMouseListener(new displaySubmenuMouseListener());
          displayNucleicAcidPopupSubmenu = new JPopupMenu();

//          displayNucleicAcidPopupSubmenu.addMouseListener(new displaySubmenuMouseListener()); 

          displayPopupMenu.add(displayNucleicAcidSubmenu);

          displayVitaminSubmenu = new JMenu("      Vitamins");
          displayVitaminSubmenu.addMouseListener(new displaySubmenuMouseListener());
          displayVitaminPopupSubmenu = new JPopupMenu();

//          displayVitaminPopupSubmenu.addMouseListener(new displaySubmenuMouseListener()); 

          displayPopupMenu.add(displayVitaminSubmenu);

//          displayPopupMenu.setLightWeightPopupEnabled(false);
//          displayPopupMenu.setVisible(false);    

          menuBar.add(displayMenu);


////////////
//
//  GUI:

          // Create Force Field popup menu associated with Force Field menu
          forceFieldPopupMenu = new JPopupMenu();

/*
               // Add popup submenu items
               charmmSubmenu = new JMenu(" CHARMM");
               charmmSubmenu.addMouseListener(new modForceFieldSubmenuMouseListener());
               charmmPopupSubmenu = new JPopupMenu();

                    // Add popup submenu items
                    allAtomItem = new JMenuItem("CHARMM All Atom FF");
                    allAtomItem.addMouseListener(new class1SubmenuMouseListener());
                    charmmPopupSubmenu.add(allAtomItem);

allAtomItem.setEnabled(false);

                    unitedAtomItem = new JMenuItem(" CHARMM United Atom FF");
                    unitedAtomItem.addMouseListener(new class1SubmenuMouseListener());
                    charmmPopupSubmenu.add(unitedAtomItem);

unitedAtomItem.setEnabled(false);

                    cgenffItem = new JMenuItem(" CGenFF");
                    cgenffItem.addMouseListener(new class1SubmenuMouseListener());
                    charmmPopupSubmenu.add(cgenffItem);
                    charmmPopupSubmenu.setLightWeightPopupEnabled(false);
                    charmmPopupSubmenu.setVisible(false);  
                    charmmSubmenu.add(charmmPopupSubmenu);

               forceFieldPopupMenu.add(charmmSubmenu);
*/

///*
          empiricalSubmenu = new JMenu(" Empirical");
          empiricalSubmenu.addMouseListener(new forceFieldSubmenuMouseListener());
          empiricalPopupSubmenu = new JPopupMenu();
               // Add popup submenu items
               class1additiveSubmenu = new JMenu("Class I   (additive)");
               class1additiveSubmenu.addMouseListener(new empiricalSubmenuMouseListener());

               class1additivePopupSubmenu = new JPopupMenu();
               // Add submenu items
                    amberItem = new JMenuItem(" Amber");

                    amberItem.addMouseListener(new class1SubmenuMouseListener());

//                    class1additivePopupSubmenu.add(amberItem);

                    charmmSubmenu = new JMenu(" CHARMM");
                    charmmSubmenu.addMouseListener(new class1SubmenuMouseListener());
                    charmmPopupSubmenu = new JPopupMenu();
                         // Add popup submenu items
                      allAtomItem = new JMenuItem("CHARMM All Atom FF");
                      allAtomItem.addMouseListener(new class1SubmenuMouseListener());
                      charmmPopupSubmenu.add(allAtomItem);

allAtomItem.setEnabled(false);

                      unitedAtomItem = new JMenuItem(" CHARMM United Atom FF");
                      unitedAtomItem.addMouseListener(new class1SubmenuMouseListener());
                      charmmPopupSubmenu.add(unitedAtomItem);

unitedAtomItem.setEnabled(false);

                      cgenffItem = new JMenuItem(" CGenFF");
                      cgenffItem.addMouseListener(new class1SubmenuMouseListener());
                      charmmPopupSubmenu.add(cgenffItem);
                      charmmPopupSubmenu.setLightWeightPopupEnabled(false);
                      charmmPopupSubmenu.setVisible(false);  
                      charmmSubmenu.add(charmmPopupSubmenu);

                    class1additivePopupSubmenu.add(charmmSubmenu);

                    cvffItem = new JMenuItem(" CVFF");
                    cvffItem.addMouseListener(new class1SubmenuMouseListener());
//                    class1additivePopupSubmenu.add(cvffItem);

                    eceppItem = new JMenuItem(" ECEPP");
                    eceppItem.addMouseListener(new class1SubmenuMouseListener());
//                    class1additivePopupSubmenu.add(eceppItem);

                    gromacsItem = new JMenuItem(" Gromacs");
                    gromacsItem.addMouseListener(new class1SubmenuMouseListener());
//                    class1additivePopupSubmenu.add(gromacsItem);

                    gromosItem = new JMenuItem(" Gromos");
                    gromosItem.addMouseListener(new class1SubmenuMouseListener());
//                    class1additivePopupSubmenu.add(gromosItem);

                    oplsSubmenu = new JMenu(" OPLS");
                    oplsSubmenu.addMouseListener(new class1SubmenuMouseListener());
                    oplsPopupSubmenu = new JPopupMenu();
                         // Add popup submenu items
                         oplsaaItem = new JMenuItem(" OPLS-AA");
                         oplsaaItem.addMouseListener(new oplsSubmenuMouseListener());
                         oplsPopupSubmenu.add(oplsaaItem);
                         oplsuaItem = new JMenuItem(" OPLS-UA");
                         oplsuaItem.addMouseListener(new oplsSubmenuMouseListener());
                         oplsPopupSubmenu.add(oplsuaItem);
                         opls2001Item = new JMenuItem(" OPLS-2001");
                         opls2001Item.addMouseListener(new oplsSubmenuMouseListener());
                         oplsPopupSubmenu.add(opls2001Item);
                         opls2005Item = new JMenuItem(" OPLS-2005");
                         opls2005Item.addMouseListener(new oplsSubmenuMouseListener());
                         oplsPopupSubmenu.add(opls2005Item);
                    oplsPopupSubmenu.setLightWeightPopupEnabled(false);
                    oplsPopupSubmenu.setVisible(false);  
//                    oplsSubmenu.add(oplsPopupSubmenu);

//                    class1additivePopupSubmenu.add(oplsSubmenu);

                    saapItem = new JMenuItem(" SAAP");
                    saapItem.addMouseListener(new class1SubmenuMouseListener());
//                    class1additivePopupSubmenu.add(saapItem);

                    class1additivePopupSubmenu.setLightWeightPopupEnabled(false);
                    class1additivePopupSubmenu.setVisible(false);  

               empiricalPopupSubmenu.add(class1additiveSubmenu);
               class2additiveSubmenu = new JMenu("Class II  (additive)");
               class2additiveSubmenu.addMouseListener(new empiricalSubmenuMouseListener());
//               empiricalPopupSubmenu.add(class2additiveSubmenu);
               coarseGrainedSubmenu = new JMenu("Course-grained");
               coarseGrainedSubmenu.addMouseListener(new empiricalSubmenuMouseListener());
//               empiricalPopupSubmenu.add(coarseGrainedSubmenu);
               manyBodySubmenu = new JMenu("Many-body");
               manyBodySubmenu.addMouseListener(new empiricalSubmenuMouseListener());
//               empiricalPopupSubmenu.add(manyBodySubmenu);
               specialFFSubmenu = new JMenu("Special FF");
               specialFFSubmenu.addMouseListener(new empiricalSubmenuMouseListener());
//               empiricalPopupSubmenu.add(specialFFSubmenu);
               userDefinedSubmenu = new JMenu("User-defined FF");
               userDefinedSubmenu.addMouseListener(new empiricalSubmenuMouseListener());
//               empiricalPopupSubmenu.add(userDefinedSubmenu);

          empiricalPopupSubmenu.setLightWeightPopupEnabled(false);
          empiricalPopupSubmenu.setVisible(false); 

          empiricalSubmenu.add(empiricalPopupSubmenu);

//          forceFieldPopupMenu.add(empiricalSubmenu);

          semiempiricalSubmenu = new JMenu(" Semi-empirical");
          semiempiricalSubmenu.addMouseListener(new forceFieldSubmenuMouseListener());
          semiempiricalPopupSubmenu = new JPopupMenu();
//          forceFieldPopupMenu.add(semiempiricalSubmenu);
       
//          forceFieldPopupMenu.addSeparator();

          coarseGrainedSubmenu = new JMenu(" Coarse-grained");
          coarseGrainedSubmenu.addMouseListener(new forceFieldSubmenuMouseListener());
          coarseGrainedPopupSubmenu = new JPopupMenu();
//          forceFieldPopupMenu.add(coarseGrainedSubmenu);

          manyBodySubmenu = new JMenu(" Many-body");
          manyBodySubmenu.addMouseListener(new forceFieldSubmenuMouseListener());
          manyBodyPopupSubmenu = new JPopupMenu();
//          forceFieldPopupMenu.add(manyBodySubmenu);

          specialFFSubmenu = new JMenu(" Special");
          specialFFSubmenu.addMouseListener(new forceFieldSubmenuMouseListener());
          specialFFPopupSubmenu = new JPopupMenu();
//          forceFieldPopupMenu.add(specialFFSubmenu);

//          forceFieldPopupMenu.addSeparator();

          userDefinedSubmenu = new JMenu(" User-defined");
          userDefinedSubmenu.addMouseListener(new forceFieldSubmenuMouseListener());
          userDefinedPopupSubmenu = new JPopupMenu();
//          forceFieldPopupMenu.add(userDefinedSubmenu);

//*/

          forceFieldPopupMenu.setLightWeightPopupEnabled(false);
          forceFieldPopupMenu.setVisible(false);  

          menuBar.add(forceFieldMenu);

// Initially disable 'Force Field' menu item 
forceFieldMenu.setEnabled(false);

          // Create Parameters popup menu associated with Parameters menu
          parametersPopupMenu = new JPopupMenu();
          // Add popup submenu items
               generateAtomTypesItem = new JMenuItem("Generate atom types"); 
               generateAtomTypesItem.addMouseListener(new paramPopupMenuMouseListener());
               parametersPopupMenu.add(generateAtomTypesItem);
               assignParamValueItem = new JMenuItem("Assign initial parameter values");      
               assignParamValueItem.addMouseListener(new paramPopupMenuMouseListener());
               parametersPopupMenu.add(assignParamValueItem);
               viewParamItem = new JMenuItem("View current parameter values");      
               viewParamItem.addMouseListener(new paramPopupMenuMouseListener());
               parametersPopupMenu.add(viewParamItem);
               parametersPopupMenu.addSeparator();
               searchDBItem = new JMenuItem("Search optimized parameter database");      
               searchDBItem.addMouseListener(new paramPopupMenuMouseListener());
               parametersPopupMenu.add(searchDBItem);
          parametersPopupMenu.setLightWeightPopupEnabled(false);
          parametersPopupMenu.setVisible(false);  

          parametersMenu.add(parametersPopupMenu);

          menuBar.add(parametersMenu);


JMenuItem gaussianScanInfileItem, runGaussianScanItem, generateMultiplicitiesItem;

          // Create Run popup menu associated with Run menu
          runPopupMenu = new JPopupMenu();
          // Add popup menu items
               gaussianScanInfileItem = new JMenuItem("Generate Gaussian scan input file");
               gaussianScanInfileItem.addMouseListener(new runSubmenuMouseListener());
               runPopupMenu.add(gaussianScanInfileItem);

               runGaussianScanItem = new JMenuItem("Run Gaussian scan");
               runGaussianScanItem.addMouseListener(new runSubmenuMouseListener());
               runPopupMenu.add(runGaussianScanItem);

runPopupMenu.addSeparator();

               generateMultiplicitiesItem = new JMenuItem("Generate multiplicities");
               generateMultiplicitiesItem.addMouseListener(new runSubmenuMouseListener());
               runPopupMenu.add(generateMultiplicitiesItem);

runPopupMenu.addSeparator();

//               generateAtomTypesItem_02 = new JMenuItem("Generate initial guess"); 
//               generateAtomTypesItem_02.addMouseListener(new runSubmenuMouseListener());
//               runPopupMenu.add(generateAtomTypesItem_02);


               dihedralOptimizationItem = new JMenuItem("Optimize dihedral parameters");
               dihedralOptimizationItem.addMouseListener(new runSubmenuMouseListener());
               runPopupMenu.add(dihedralOptimizationItem);
               
            // added by ning for xyplot run since the menu of monitor does not show up properly 
               runPopupMenu.addSeparator();
               xyplotMenuItem = new JMenuItem("XYPlot");
               xyplotMenuItem.addMouseListener(new monitorSubmenuMouseListener());
               runPopupMenu.add(xyplotMenuItem);

          runPopupMenu.setLightWeightPopupEnabled(false);
          runPopupMenu.setVisible(false);  

          runMenu.add(runPopupMenu);

          menuBar.add(runMenu);

          // Create Display popup menu associated with Display menu
          //   displayPopupMenu = new JPopupMenu();

          //   menuBar.add(displayMenu);

          // Create Settings popup menu associated with Settings menu
          settingsPopupMenu = new JPopupMenu();

          menuBar.add(settingsMenu);

          // Create help popup menu associated with Help menu
          helpPopupMenu = new JPopupMenu();

          menuBar.add(helpMenu);


////////////
//
//  GUI:

dhLabel = new JLabel("Dihedral atoms:");  dhLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
dhAtmTypLabel = new JLabel("Dihedral atom types:");  dhAtmTypLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
svLabel = new JLabel("Starting value (deg):");  svLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
evLabel = new JLabel("Ending value (deg):");  evLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));
stpsizLabel = new JLabel("Step size (deg):");  stpsizLabel.setFont(new Font("Lucinda Console", Font.BOLD, 13));

dhAtomsTxt = new JTextField(16); dhAtomsTxt.setFont(new Font("Lucinda Console", Font.BOLD, 12)); dhAtomsTxt.setEditable(false);  dhAtomsTxt.setHorizontalAlignment(JTextField.CENTER);
dhAtmTypTxt = new JTextField(20); dhAtmTypTxt.setFont(new Font("Lucinda Console", Font.BOLD, 12)); dhAtmTypTxt.setEditable(false);  dhAtmTypTxt.setHorizontalAlignment(JTextField.CENTER);

dhAtmTypTxtArea = new JTextArea();  dhAtmTypTxtArea.setFont(new Font("Lucinda Console", Font.BOLD, 12));  // dhAtmTypTxtArea.setEditable(false);


// Set default values for starting value (0), ending value (360), and step size (15)
svTxt = new JTextField("0", 6); svTxt.setFont(new Font("Lucinda Console", Font.BOLD, 12)); svTxt.setHorizontalAlignment(JTextField.RIGHT);
evTxt = new JTextField("360", 6); evTxt.setFont(new Font("Lucinda Console", Font.BOLD, 12)); evTxt.setHorizontalAlignment(JTextField.RIGHT);
stpsizTxt = new JTextField("15", 6); stpsizTxt.setFont(new Font("Lucinda Console", Font.BOLD, 12)); stpsizTxt.setHorizontalAlignment(JTextField.RIGHT);

dhSubmitBtn = new JButton("Submit");
dhCancelBtn = new JButton("Cancel");

bAdd = new JButton(" Add ");
bAdd.addActionListener(new paramButtonActionListener());
bEdit = new JButton("Edit");
bEdit.addActionListener(new paramButtonActionListener());
bCancel = new JButton("Cancel");
bCancel.addActionListener(new paramButtonActionListener());
bSave = new JButton("Save");
bSave.addActionListener(new paramButtonActionListener());

pBondParams1 = new JPanel();
pBondParams2 = new JPanel();
pSelectedBond = new JPanel();
pAtomTypes = new JPanel();
pButtons = new JPanel();

tfKb = new JTextField(10);          tfKb.setFont(new Font("Lucinda Console", Font.BOLD, 12));     tfKb.setHorizontalAlignment(JTextField.CENTER); 
tfR0 = new JTextField(10);          tfR0.setFont(new Font("Lucinda Console", Font.BOLD, 12));     tfR0.setHorizontalAlignment(JTextField.CENTER); 
tfAtomTypes = new JTextField(12);   tfAtomTypes.setFont(new Font("Lucinda Console", Font.BOLD, 12));     tfAtomTypes.setHorizontalAlignment(JTextField.CENTER); 

cmbSelectedBonds.addActionListener(new cmbSelectedBondsActionListener());
cmbSelectedBonds.setPreferredSize(new Dimension(125, 20));    cmbSelectedBonds.setFont(new Font("Lucinda Console", Font.BOLD, 12));     
//cmbSelectedBonds.setHorizontalAlignment(JComboBox.CENTER); 

// Construct JFrame for force field parametrization 
paramFrame = new JFrame("Force Field Parameters");
paramFrame.addWindowListener(new WindowAdapter() 
{
     public void windowClosing(WindowEvent we)
     {
          paramFrame.setVisible(false);
     }
});

paramFrame.setSize(300, 275);
paramFrame.setLocation(200, 200);
paramFrame.setAlwaysOnTop(true);
paramFrame.setVisible(false);


// Construct JFrame for optimization of dihedral parameters
dhOptFrame = new JFrame("Dihedral Optimization");
dhOptFrame.addWindowListener(new WindowAdapter() 
{
     public void windowClosing(WindowEvent we)
     {
          paramFrame.setVisible(false);
     }
});

dhOptFrame.setSize(300, 275);
dhOptFrame.setLocation(200, 200);
dhOptFrame.setAlwaysOnTop(true);

dhSelection1Rb = new JRadioButton("Optimize selected dihedral only");
dhSelection2Rb = new JRadioButton("Optimize all equivalent dihedrals");
dhSelection1Rb.setEnabled(false);  dhSelection2Rb.setEnabled(false);


dhBtnGrp = new ButtonGroup();
dhBtnGrp.add(dhSelection1Rb);  dhBtnGrp.add(dhSelection2Rb);

dhOptPane = new JPanel();
dhOptPane.setLayout(new GridBagLayout());

GridBagConstraints con = new GridBagConstraints();

// Add labels and text fields
con.insets = new Insets(20, 10, 5, 10);
con.gridx = 0;  con.gridy = 0;
con.anchor = GridBagConstraints.LINE_END;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(dhLabel, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(20, 15, 5, 15);
con.gridx = 1; 
con.anchor = GridBagConstraints.LINE_START;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(dhAtomsTxt, con);

con.insets = new Insets(10, 10, 5, 10);
con.gridx = 0;  con.gridy = 1;
con.anchor = GridBagConstraints.LINE_END;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(dhAtmTypLabel, con);

     dhAtmTypeTxtAreaScrollPane = new JScrollPane(dhAtmTypTxtArea);
//     dhAtmTypeTxtAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
     dhAtmTypeTxtAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
     dhAtmTypeTxtAreaScrollPane.setPreferredSize(new Dimension(200, 50));

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(10, 15, 5, 15);
con.gridx = 1; 
con.anchor = GridBagConstraints.LINE_START;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(dhAtmTypeTxtAreaScrollPane, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(10, 10, 5, 15);
con.gridx = 0; con.gridy = 2;
con.anchor = GridBagConstraints.LINE_END;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(svLabel, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.gridx = 1; 
con.insets = new Insets(10, 15, 5, 15);
con.anchor = GridBagConstraints.LINE_START;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(svTxt, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(10, 10, 5, 15);
con.gridx = 0; con.gridy = 3;
con.anchor = GridBagConstraints.LINE_END;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(evLabel, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(10, 15, 5, 15);
con.gridx = 1; 
con.anchor = GridBagConstraints.LINE_START;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(evTxt, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(10, 10, 5, 15);
con.gridx = 0; con.gridy = 4;
con.anchor = GridBagConstraints.LINE_END;
//con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(stpsizLabel, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(10, 15, 5, 15);
con.gridx = 1; 
con.anchor = GridBagConstraints.LINE_START;
dhOptPane.add(stpsizTxt, con);

// Add radio buttons
con.insets = new Insets(15, 20, 2, 2);
con.gridwidth = 2;
con.gridx = 0; con.gridy = 5;
con.anchor = GridBagConstraints.LINE_START;
dhOptPane.add(dhSelection1Rb, con);

con.insets = new Insets(5, 20, 2, 2);
con.gridwidth = 2;
con.gridx = 0; con.gridy = 6;
con.anchor = GridBagConstraints.LINE_START;
dhOptPane.add(dhSelection2Rb, con);

// Add buttons
//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(25, 20, 10, 2);
con.gridwidth = 1;
con.gridx = 0; con.gridy = 7;
con.anchor = GridBagConstraints.LINE_END;
dhOptPane.add(dhSubmitBtn, con);

//con.fill = GridBagConstraints.HORIZONTAL;
con.insets = new Insets(25, 5, 10, 15);
con.gridwidth = 1;
con.gridx = 1; 
con.anchor = GridBagConstraints.CENTER;
dhOptPane.add(dhCancelBtn, con);


dhOptFrame.getContentPane().add(dhOptPane, BorderLayout.CENTER);
dhOptFrame.pack();

dhOptFrame.setVisible(false);


////////////////////////////////////////


JPanel bondpanel = new JPanel();
JPanel anglepanel = new JPanel();
JPanel dihedralpanel = new JPanel();
JPanel improperspanel = new JPanel();
JPanel UBpanel = new JPanel();
JPanel nonbonedpanel = new JPanel();


/////

// JPanel with SpringLayout for dihedral optimization
JPanel jpDihedralOpt = new JPanel(new SpringLayout());
JLabel lbDihedralAtoms, lbStepSize, lbStartingValue, lbEndingValue;
JTextField txtDihedralAtoms, txtStepSize, txtStartingValue, txtEndingValue;

//SpringPanel spDihedralOpt = new SpringPanel();
SpringLayout splayout = new SpringLayout();
//splayout.add(lbDihedralAtoms);

lbDihedralAtoms = new JLabel(" Dihedral atoms: ");

//splayout.putConstraint(SpringLayout.WEST, lbDihedralAtoms, 10, SpringLayout.WEST, this);

/////


lbKb = new JLabel("<html>k<sub>b</sub>:</html>");
lbR0 = new JLabel("<html>r<sub>0</sub>:</html>");
lbBondID = new JLabel("Bond ID:");
lbAtomTypes = new JLabel("Atom types:");

pBondParams1.setPreferredSize(new Dimension(10, 5));
pBondParams1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 15));
pBondParams2.setPreferredSize(new Dimension(10, 5));
pBondParams2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 4));
//pBondParams.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

pBondParams1.add(lbKb);  
pBondParams1.add(tfKb); 
pBondParams2.add(lbR0);  
pBondParams2.add(tfR0); 

pSelectedBond.setPreferredSize(new Dimension(10, 50));
pSelectedBond.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 4));
pSelectedBond.add(lbBondID);
pSelectedBond.add(cmbSelectedBonds);

pAtomTypes.setPreferredSize(new Dimension(10, 5));
pAtomTypes.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 4));
//pAtomTypes.setPreferredSize(new Dimension(30, 5));
pAtomTypes.add(lbAtomTypes);
pAtomTypes.add(tfAtomTypes);

pButtons.setPreferredSize(new Dimension(10, 5));
pButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 8));
pButtons.add(bAdd);
pButtons.add(bEdit);
pButtons.add(bCancel);
pButtons.add(bSave);

bondpanel.setLayout(new GridLayout(5, 1));
bondpanel.add(pBondParams1);
bondpanel.add(pBondParams2);
bondpanel.add(pSelectedBond);
bondpanel.add(pAtomTypes);
bondpanel.add(pButtons);

JTabbedPane paramtp = new JTabbedPane();

paramtp.addTab("    Bond    ", bondpanel);
paramtp.addTab("   Angle    ", anglepanel);
paramtp.addTab("  Dihedral  ", dihedralpanel);
paramtp.addTab(" Impropers  ", improperspanel);
paramtp.addTab("Urey-Bradley", UBpanel);
paramtp.addTab(" Non-bonded ", nonbonedpanel);

paramFrame.getContentPane().add(paramtp);


////////////


          // Construct JFrame to be used for scaling atom radii
          int fwidth, fheight;
          fwidth = 250;
          fheight = 140;    
     }

     public void centerWindow(Component frame)
     {
          screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          frameSize = frame.getSize();

          if (frameSize.width > screenSize.width)
          {
               frameSize.width = screenSize.width;
          }
          if (frameSize.height > screenSize.height)
          {
               frameSize.height = screenSize.height;
          }

          frame.setLocation(0, (screenSize.height - frameSize.height)/2);
     }



public class jtableCellRenderer extends DefaultTableCellRenderer
{
     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int colIndex)
     {
          int selectedRow, selectedCol;
          int selectedDHParamtersTableCol;
          int ntblrows;

          int[] selectedTblRows;
          int[] selectedTblCols;

          String dhToolTipText;

          selectedTblRows = dhParametersTable.getSelectedRows();

          selectedTblCols = dhParametersTable.getSelectedColumns();
          selectedDHParamtersTableCol = dhParametersTable.getSelectedColumn();


//System.out.print("selectedTblCols = " + selectedTblCols[k] + " ");
//System.out.print("selectedTblCols length = " + selectedTblCols.length);



          tblCell = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, colIndex);

          tblCell.setFont(new Font("Arial", Font.BOLD, 12));

          tblCell.setHorizontalAlignment(SwingConstants.CENTER);

          if (natoms > 0)
          {

//selectedRow = dhParametersTable.getSelectedRow();
//dhToolTipText = "Row " + selectedRow + " has been selected";


//               tableCellRenderer.setToolTipText(getDhToolTip());

//               tableCellRenderer.setToolTipText(dhToolTipText);

//System.out.println("row that has been selected = " + selectedRow);


               if (selectedDHParamtersTableCol == 1)
               {
                    dhParametersTable.setSelectionBackground(Color.red);
                    dhParametersTable.setSelectionForeground(Color.white);

System.out.println(dhParametersTable.getValueAt(selectedTblRows[0], 1));

idCentralDhBond = true;

               } 
               else if (selectedDHParamtersTableCol == 0)
               {
                    dhParametersTable.setSelectionBackground(Color.white);
                    dhParametersTable.setSelectionForeground(Color.black);

dhToolTipText = "Row " + selectedTblRows[0] + " has been selected";
tableCellRenderer.setToolTipText(dhToolTipText);

               }
          }
 
          return tblCell;
     }
}



public void setRowNdxList(ArrayList<Integer> rowndxList)
{
     if (rowndxList.size() > 0)
     {
//          System.out.println(rowndxList.get(0));       
     }
}


public ArrayList<Integer> getRowNdxList()
{
     return rowndxList;
}


public String getDhToolTip()
{
     int rowIndx, colIndx, realColIndx;

     
     dhToolTipText = new String();

     dhToolTipText = "Hello there";

     return dhToolTipText;
}



// ???????????????????????????


     public void printCoords(double x, double y, double z)
     {
          System.out.println("x coordinate: " + x);
          System.out.println("y coordinate: " + y);
          System.out.println("z coordinate: " + z);
     }

     public void hideSubmenus()
     {
          // Hide all submenus
          projectionSubmenu.setVisible(false);
          importSubmenu.setVisible(false);
          exportSubmenu.setVisible(false);
          createMoleculeSubmenu.setVisible(false);
//          drawAtomSubmenu.setVisible(false);
          editMoleculeSubmenu.setVisible(false);
          createNanostructureSubmenu.setVisible(false);

          displayAminoAcidSubmenu.setVisible(false);
          displayDrugSubmenu.setVisible(false);
          displayLipidSubmenu.setVisible(false);
          displayNucleicAcidSubmenu.setVisible(false);
          displaySugarSubmenu.setVisible(false);
     } 

     public void hidePopupMenus()
     {
          // Hide all popup menus
          filePopupMenu.setVisible(false);
          editPopupMenu.setVisible(false);
          viewPopupMenu.setVisible(false);
          structurePopupMenu.setVisible(false);
          forceFieldPopupMenu.setVisible(false);
          displayPopupMenu.setVisible(false);
          runPopupMenu.setVisible(false);
     }

     public void hidePopupSubmenus()
     {
          // Hide all popup submenus
          importPopupSubmenu.setVisible(false);
          exportPopupSubmenu.setVisible(false);
          displayStylePopupSubmenu.setVisible(false);
          projectionPopupSubmenu.setVisible(false);

          createMoleculePopupSubmenu.setVisible(false);
          editMoleculePopupSubmenu.setVisible(false);
//          drawFcnalGrpPopupSubmenu.setVisible(false);
//          drawFragmentPopupSubmenu.setVisible(false);
          createNanostructurePopupSubmenu.setVisible(false);
          hybridizationPopupSubmenu.setVisible(false);

          class1additivePopupSubmenu.setVisible(false);
          empiricalPopupSubmenu.setVisible(false);
          charmmPopupSubmenu.setVisible(false);
          oplsPopupSubmenu.setVisible(false);
          parametersPopupMenu.setVisible(false);

//          displayAminoAcidPopupSubmenu.setVisible(false);
          displayDrugPopupSubmenu.setVisible(false);
          displayLipidPopupSubmenu.setVisible(false);
          displayNucleicAcidPopupSubmenu.setVisible(false);
//          displaySugarPopupSubmenu.setVisible(false);
          displayCofactorPopupSubmenu.setVisible(false);
          displayVitaminPopupSubmenu.setVisible(false);
     }

     public double[][] getCenteredCoords(int natoms, double[][] atomCoordinates)
     {
          double[] minmaxCoords = new double[2*ncoords];

          for (indx = 0; indx < ndims; indx++)
          {
               minmaxCoords[indx] = minmaxCoords[(indx+3)] = atomCoordinates[0][indx];
          }

          for (j = 1; j < natoms; j++)
          {
               for (k = 0; k < ncoords; k++)
               {
                    if (atomCoordinates[j][k] < minmaxCoords[k])
                    {
                         minmaxCoords[k] = atomCoordinates[j][k];
                    } 

                    if (atomCoordinates[j][k] > minmaxCoords[(k+3)])
                    {
                         minmaxCoords[(k+3)] = atomCoordinates[j][k];
                    } 
               }
          }

          for (k = 0; k < ncoords; k++)
          {
               delCoords[k] = (minmaxCoords[k] + minmaxCoords[(k+3)])/2;
          }

          // Center atom coordinates
          for (j = 0; j < natoms; j++)
          {
//               for (k = 0; k < ncoords; k++)
               for (k = 0; k < ncoords; k++)
               {
                    atomCoordinates[j][(k+1)] -= delCoords[k];
                 
                    strval = Double.toString(atomCoordinates[j][(k+1)]);
                    int pointNdx = strval.indexOf(".");

                    strlngth = Math.min( (strval.length() - pointNdx), pointNdx+6); 
                    strval = strval.substring(0, strlngth);

                    atomCoordinates[j][(k+1)] = Double.valueOf(strval);
               }  
          }

          return atomCoordinates;
     }

     public void setDelCoords(int natoms, double[][] coordinates)
     {
          double[] minmaxCoords = new double[2*ncoords];

          for (indx = 0; indx < ndims; indx++)
          {
               minmaxCoords[indx] = minmaxCoords[(indx+3)] = coordinates[0][indx];
          }

          for (j = 1; j < natoms; j++)
          {
               for (k = 0; k < ncoords; k++)
               {
                    if (coordinates[j][k] < minmaxCoords[k])
                    {
                         minmaxCoords[k] = coordinates[j][k];

                    } 

                    if (coordinates[j][k] > minmaxCoords[(k+3)])
                    {
                         minmaxCoords[(k+3)] = coordinates[j][k];
                    } 
               }
          }

          for (k = 0; k < ncoords; k++)
          {
               delCoords[k] = (minmaxCoords[k] + minmaxCoords[(k+3)])/2;
          }
     }

     public double[] getDelCoords()
     {
          return delCoords;
     }

     public ArrayList<Double> getAtomRadii(ArrayList<Integer> AN)
     {
          int anNdx;

          double radius;
          double rad_C = 0.35;
          double[ ] covrad  =  {0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00};         
          double covrad_C = covrad[5];

          atomRadii.clear();

          for (ndx = 0; ndx < AN.size(); ndx++)
          {
               anNdx = AN.get(ndx) - 1;

               // Set radius relative to covalent radius of carbon atom
               radius = (covrad[anNdx]/covrad_C) * rad_C;

               atomRadii.add(radius);
          }

          return atomRadii;          
     }

     public void setBondAngleFragments(int natoms, double[][] coordinates, int[][] connectivity, int end1AtomNdx, int end2AtomNdx, int centralAtomNdx)
     {
          ArrayList<Integer> bondedAtoms = new ArrayList<Integer>();
          ArrayList<Integer> fragmentAtoms = new ArrayList<Integer>();
          ArrayList<Integer> fragment1Atoms = new ArrayList<Integer>();
          ArrayList<Integer> fragment2Atoms = new ArrayList<Integer>();

          bondAngleFragments = new ArrayList<ArrayList<Integer>>();

          int ndx1, ndx2, bvndx;
//          int nfragments, minFragmentNum, maxFragmentNum, nfragmentAtoms, fragmentAtomNdx;
          int minFragmentNum, maxFragmentNum, nfragmentAtoms, fragmentAtomNdx;

          int[] selectedAtomIndices; 
          int [][] fragmentConnectivity;

          double dx1, dy1, dz1, dx2, dy2, dz2, R12, nnx, nny, nnz;
          double[][] fragmentCoordinates;

          boolean bondFound;

          for (indx = 0; indx < 2; indx++)
          {
               fragmentAtoms.clear();

               if (indx == 0)
               {
                    selectedAtomNdx = end1AtomNdx;
               }
               else if (indx == 1)
               {
                    selectedAtomNdx = end2AtomNdx;
               }

               fragmentConnectivity = new int[natoms][natoms];

               bondedAtoms.clear();

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < natoms; k++)
                    {
                         fragmentConnectivity[j][k] = connectivity[j][k];
                    }
               }

               fragmentConnectivity[selectedAtomNdx][centralAtomNdx] = fragmentConnectivity[centralAtomNdx][selectedAtomNdx] = 0;

               bondFound = true;

               while (bondFound)
               {
                    if (fragmentAtoms.contains(selectedAtomNdx) == false) 
                    {
                         fragmentAtoms.add(selectedAtomNdx);

                         bondedAtoms.add(selectedAtomNdx);
                    }

                    for (k = 0; k < natoms; k++)
                    {
                         if (fragmentConnectivity[selectedAtomNdx][k] != 0)
                         {
                              fragmentAtoms.add(k);
 
                              bondedAtoms.add(k);

                              fragmentConnectivity[selectedAtomNdx][k] = fragmentConnectivity[k][selectedAtomNdx] = 0;
                         }
                    }               

                    bondedAtoms.remove(0);

                    if (bondedAtoms.size() != 0)
                    {
                         selectedAtomNdx = bondedAtoms.get(0);
                    }
                    else
                    {
                         bondFound = false;
                    }
               }

               selectedFragmentArray.clear();

               nfragmentAtoms = fragmentAtoms.size();

               if (fragmentAtoms.size() > 0)
               {
                    for (ndx = 0; ndx < nfragmentAtoms; ndx++)
                    {
                         if ( (indx == 0) && (fragmentAtoms.get(ndx) != centralAtomNdx) )
                         {                             
                              if ( !fragment1Atoms.contains(fragmentAtoms.get(ndx)) )
                              {
                                   fragment1Atoms.add(fragmentAtoms.get(ndx));
                              }
                         }
                         else if ( (indx == 1) && (fragmentAtoms.get(ndx) != centralAtomNdx) )
                         {
                              if ( !fragment2Atoms.contains(fragmentAtoms.get(ndx)) )
                              {
                                   fragment2Atoms.add(fragmentAtoms.get(ndx));
                              }
                         }                             
                    }
               }
          }

          bondAngleFragments.add(fragment1Atoms);
          bondAngleFragments.add(fragment2Atoms);

          fragment1BV.clear();  fragment2BV.clear();  fragmentBV.clear();
          
          for (ndx1 = 0; ndx1 < fragment1Atoms.size(); ndx1++)
          {
               for (ndx2 = 0; ndx2 < bondVectors.length; ndx2++)
               {
                    bvndx = ( -((int)(bondVectors[ndx2][0]/100) + 1) );     

                    if (bvndx == fragment1Atoms.get(ndx1))
                    {
                         fragment1BV.add(ndx2);

                         fragmentBV.add(ndx2);
                    }
               }
          }

          for (ndx1 = 0; ndx1 < fragment2Atoms.size(); ndx1++)
          {
               for (ndx2 = 0; ndx2 < bondVectors.length; ndx2++)
               {
                    bvndx = ( -((int)(bondVectors[ndx2][0]/100) + 1) );     

                    if (bvndx == fragment2Atoms.get(ndx1))
                    {
                         fragment2BV.add(ndx2);

                         fragmentBV.add(ndx2);
                    }
               }
          }

          rotationOriginIndex = centralAtomNdx;

          // Set rotation axis for bond angle 
          dx1 = coordinates[end1AtomNdx][1] - coordinates[centralAtomNdx][1];
          dy1 = coordinates[end1AtomNdx][2] - coordinates[centralAtomNdx][2];
          dz1 = coordinates[end1AtomNdx][3] - coordinates[centralAtomNdx][3];

          dx2 = coordinates[end2AtomNdx][1] - coordinates[centralAtomNdx][1];
          dy2 = coordinates[end2AtomNdx][2] - coordinates[centralAtomNdx][2];
          dz2 = coordinates[end2AtomNdx][3] - coordinates[centralAtomNdx][3];

          // Compute bond rotation axis 
          nnx = dy1*dz2 - dy2*dz1;  nny = dz1*dx2 - dz2*dx1;  nnz = dx1*dy2 - dx2*dy1;

          R12 = Math.sqrt( (nnx*nnx) + (nny*nny) + (nnz*nnz) );

          // Compute unit vector along bond rotation axis
          bondAngleRotationAxis = new double[ndimensions];

          bondAngleRotationAxis[0] = nnx/R12;  bondAngleRotationAxis[1] = nny/R12;  bondAngleRotationAxis[2] = nnz/R12; 

          bondAngleSelected = true;
     }

     public void setAxialFragmentAtoms(int natoms, double[][] coordinates, int[][] connectivity, int axisAtom1, int axisAtom2)
     {
          ArrayList<Integer> bondedAtoms = new ArrayList<Integer>();

          int selectedAtomNdx;
          int [][] fragmentConnectivity;

          boolean bondFound;

          fragmentConnectivity = new int[natoms][natoms];

          bondedAtoms.clear();

          for (j = 0; j < natoms; j++)
          {
               for (k = 0; k < natoms; k++)
               {
                    fragmentConnectivity[j][k] = connectivity[j][k];
               }
          }
  
          fragmentConnectivity[axisAtom2][axisAtom1] = fragmentConnectivity[axisAtom1][axisAtom2] = 0;

          selectedAtomNdx = axisAtom2;

          axialFragmentAtoms.clear();

          bondFound = true;

          while (bondFound)
          {
               if (!axialFragmentAtoms.contains(selectedAtomNdx)) 
               {
                    axialFragmentAtoms.add(selectedAtomNdx);
 
                    bondedAtoms.add(selectedAtomNdx);
               }

               for (k = 0; k < natoms; k++)
               {
                    if ( (fragmentConnectivity[selectedAtomNdx][k] != 0) && (!axialFragmentAtoms.contains(k)) )
                    {
                         axialFragmentAtoms.add(k);

                         bondedAtoms.add(k);

                         fragmentConnectivity[selectedAtomNdx][k] = fragmentConnectivity[k][selectedAtomNdx] = 0;
                    }
               }               

               if (bondedAtoms.size() == 0)
               {
                    break;
               }
               else
               {
                    bondedAtoms.remove(0);
               }

               if (bondedAtoms.size() != 0)
               {
                    selectedAtomNdx = bondedAtoms.get(0);
               }
               else
               {
                    bondFound = false;
               }
          }

          rotationOriginIndex = axisAtom2;

          // Set rotation axis for axial rotation (direction is from axisAtom1 to axisAtom2)
          dx = coordinates[axisAtom2][1] - coordinates[axisAtom1][1];
          dy = coordinates[axisAtom2][2] - coordinates[axisAtom1][2];
          dz = coordinates[axisAtom2][3] - coordinates[axisAtom1][3];

          R = Math.sqrt( (dx*dx) + (dy*dy) + (dz*dz) );

          // Compute unit vector along dihedral rotation axis
          axialRotationAxis = new double[ndimensions];

          axialRotationAxis[0] = dx/R;  axialRotationAxis[1] = dy/R;  axialRotationAxis[2] = dz/R; 

          axialRotationSelected = true;
     }

     public void setDihedralAngleFragments(int natoms, double[][] coordinates, int[][] connectivity, int axisAtom1, int axisAtom2)
     {
          ArrayList<Integer> bondedAtoms = new ArrayList<Integer>();
          ArrayList<Integer> fragmentAtoms = new ArrayList<Integer>();
          ArrayList<Integer> fragment1Atoms = new ArrayList<Integer>();
          ArrayList<Integer> fragment2Atoms = new ArrayList<Integer>();

          ArrayList<Integer> dihedralFragmentAtoms = new ArrayList<Integer>();
          ArrayList<Integer> dihedralFragment1Atoms = new ArrayList<Integer>();
          ArrayList<Integer> dihedralFragment2Atoms = new ArrayList<Integer>();

          int ndx1, ndx2, bvndx;
//          int nfragments, minFragmentNum, maxFragmentNum, nfragmentAtoms, fragmentAtomNdx, centralAtomNdx;
          int minFragmentNum, maxFragmentNum, nfragmentAtoms, fragmentAtomNdx, centralAtomNdx;
          int[] selectedAtomIndices; 
          int [][] fragmentConnectivity;

          double dx, dy, dz, R, nx, ny, nz;
          double[][] fragmentCoordinates;
          
          boolean bondFound;

          selectedAtomNdx = centralAtomNdx = -1;

          for (indx = 0; indx < 2; indx++)
          {
               dihedralFragmentAtoms.clear();

               if (indx == 0)
               {
                    selectedAtomNdx = axisAtom1;
                    centralAtomNdx = axisAtom2;
               }
               else if (indx == 1)
               {                    
                    selectedAtomNdx = axisAtom2;
                    centralAtomNdx = axisAtom1;
               }

               fragmentConnectivity = new int[natoms][natoms];

               bondedAtoms.clear();

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < natoms; k++)
                    {
                         fragmentConnectivity[j][k] = connectivity[j][k];
                    }
               }
  
               fragmentConnectivity[selectedAtomNdx][centralAtomNdx] = fragmentConnectivity[centralAtomNdx][selectedAtomNdx] = 0;

               bondFound = true;

               while (bondFound)
               {
                    if (dihedralFragmentAtoms.contains(selectedAtomNdx) == false) 
                    {
                         dihedralFragmentAtoms.add(selectedAtomNdx);
 
                         bondedAtoms.add(selectedAtomNdx);
                    }

                    for (k = 0; k < natoms; k++)
                    {
                         if (fragmentConnectivity[selectedAtomNdx][k] != 0)
                         {
                              dihedralFragmentAtoms.add(k);
   
                              bondedAtoms.add(k);

                              fragmentConnectivity[selectedAtomNdx][k] = fragmentConnectivity[k][selectedAtomNdx] = 0;
                         }
                    }               

                    bondedAtoms.remove(0);

                    if (bondedAtoms.size() != 0)
                    {
                         selectedAtomNdx = bondedAtoms.get(0);
                    }
                    else
                    {
                         bondFound = false;
                    }
               }

               selectedFragmentArray.clear();

               nfragmentAtoms = dihedralFragmentAtoms.size();

               if (nfragmentAtoms > 0)
               {
                    for (ndx = 0; ndx < nfragmentAtoms; ndx++)
                    {
                         if ( (indx == 0) && (dihedralFragmentAtoms.get(ndx) != centralAtomNdx) )
                         {                             
                              dihedralFragment1Atoms.add(dihedralFragmentAtoms.get(ndx));
                         }
                         else if ( (indx == 1) && (dihedralFragmentAtoms.get(ndx) != centralAtomNdx) )
                         {                             
                              dihedralFragment2Atoms.add(dihedralFragmentAtoms.get(ndx));
                         }

                    }
               }
          }

          dihedralFragmentAtoms.clear();
          dihedralAngleFragments.clear();

          fragment1BV.clear();  fragment2BV.clear();  fragmentBV.clear();
          
          for (ndx1 = 0; ndx1 < dihedralFragment1Atoms.size(); ndx1++)
          {
               for (ndx2 = 0; ndx2 < bondVectors.length; ndx2++)
               {
                    bvndx = ( -((int)(bondVectors[ndx2][0]/100) + 1) );     

                    if (bvndx == dihedralFragment1Atoms.get(ndx1))
                    {
                         fragment1BV.add(ndx2);

                         fragmentBV.add(ndx2);
                    }
               }
          }

          for (ndx1 = 0; ndx1 < dihedralFragment2Atoms.size(); ndx1++)
          {
               for (ndx2 = 0; ndx2 < bondVectors.length; ndx2++)
               {
                    bvndx = ( -((int)(bondVectors[ndx2][0]/100) + 1) );     

                    if (bvndx == dihedralFragment2Atoms.get(ndx1))
                    {
                         fragment2BV.add(ndx2);

                         fragmentBV.add(ndx2);
                    }
               }
          }

          dx = dy = dz = 0.0;

          if ( (dihedralFragment1Atoms.size() <= dihedralFragment2Atoms.size()) && (!bvDihedralSelected) )
          {
               for (j = 0; j < dihedralFragment1Atoms.size(); j++)
               {
                    dihedralAngleFragments.add(dihedralFragment1Atoms.get(j));
               }

               rotationOriginIndex = axisAtom1;

               // Set rotation axis for dihedral angle (direction is from axisAtom2 to axisAtom1)
               dx = coordinates[axisAtom1][1] - coordinates[axisAtom2][1];
               dy = coordinates[axisAtom1][2] - coordinates[axisAtom2][2];
               dz = coordinates[axisAtom1][3] - coordinates[axisAtom2][3];
          }
          else
          {
               for (j = 0; j < dihedralFragment2Atoms.size(); j++)
               {
                    dihedralAngleFragments.add(dihedralFragment2Atoms.get(j));
               }

               rotationOriginIndex = axisAtom2;

               // Set rotation axis for dihedral angle (direction is from axisAtom1 to axisAtom2)
               dx = coordinates[axisAtom2][1] - coordinates[axisAtom1][1];
               dy = coordinates[axisAtom2][2] - coordinates[axisAtom1][2];
               dz = coordinates[axisAtom2][3] - coordinates[axisAtom1][3];
          }

          R = Math.sqrt( (dx*dx) + (dy*dy) + (dz*dz) );

         // Compute unit vector along dihedral rotation axis
         dihedralAngleRotationAxis = new double[ndimensions];

         dihedralAngleRotationAxis[0] = dx/R;  dihedralAngleRotationAxis[1] = dy/R;  dihedralAngleRotationAxis[2] = dz/R; 

         dihedralAngleSelected = true;
     }

     public ArrayList<ArrayList<Integer>> getBondAngleFragments()
     {
          return bondAngleFragments;
     }

     public double[] getBondAngleRotationAxis()
     {
          return bondAngleRotationAxis;
     }

     ArrayList<Integer> getDihedralAngleFragments()
     {
          return dihedralAngleFragments;
     }

     ArrayList<Integer> getAxialFragmentAtoms()
     {
          return axialFragmentAtoms;
     }

     ArrayList<Integer> getFragmentBV()
     {
          return fragmentBV;
     }     

     public double[] getDihedralAngleRotationAxis()
     {
          return dihedralAngleRotationAxis;
     }

     public double[] getAxialRotationAxis()
     {
          return axialRotationAxis;
     }

     public int getRotationOriginIndex()
     {
          return rotationOriginIndex;
     }

     public double[][] getFragmentAtoms(int natoms, double[][] coordinates, int[][] connectivity, int selectedAtomNdx, int unselectedAtomNdx)
     {
          // Note: If an atom has been deleted, natoms has already been reduced by 1 and the
          //       bond between selectedAtom and unselectedAtom has already been set to 0

          ArrayList<Integer> bondedAtoms = new ArrayList<Integer>();
          ArrayList<Integer> fragmentAtoms = new ArrayList<Integer>();
          

//          int nfragments, minFragmentNum, maxFragmentNum, nfragmentAtoms, fragmentAtomNdx;
          int minFragmentNum, maxFragmentNum, nfragmentAtoms, fragmentAtomNdx;

          int [][] fragmentConnectivity;
          double[][] fragmentCoordinates;

          boolean bondFound;
 
          fragmentConnectivity = new int[natoms][natoms];

          bondedAtoms.clear(); 

          // Set minimum fragment number to 1
          minFragmentNum = (int)coordinates[0][4];

          for (j = 1; j < natoms; j++)
          {
               minFragmentNum = Math.max(minFragmentNum, (int)coordinates[j][4]);
          }

          if (minFragmentNum != 1)
          {
               for (j = 0; j < natoms; j++)
               {
                    coordinates[j][4] -= (minFragmentNum - 1);
               }
          }

          // Get maximum fragment number
          maxFragmentNum = 1;

          for (j = 0; j < natoms; j++)
          {
               maxFragmentNum = Math.max(maxFragmentNum, (int)coordinates[j][4]);
          }

          nfragments = 1;

          for (j = 0; j < natoms; j++)
          {
               nfragments = Math.max(nfragments, (int)coordinates[j][4]);
          }

          for (j = 0; j < natoms; j++)
          {
               for (k = 0; k < natoms; k++)
               {
                    fragmentConnectivity[j][k] = connectivity[j][k];
               }
          }

          bondFound = true;

          while (bondFound)
          {
//               if (fragmentAtoms.contains(selectedAtomNdx) == false) 
               if (!fragmentAtoms.contains(selectedAtomNdx)) 
               {
                    fragmentAtoms.add(selectedAtomNdx);

                    bondedAtoms.add(selectedAtomNdx);
               }

               for (k = 0; k < natoms; k++)
               {
                    if (fragmentConnectivity[selectedAtomNdx][k] != 0)
                    {
                         fragmentAtoms.add(k);

                         bondedAtoms.add(k);

                         fragmentConnectivity[selectedAtomNdx][k] = fragmentConnectivity[k][selectedAtomNdx] = 0;
                    }
               }               

               bondedAtoms.remove(0);

               if (bondedAtoms.size() != 0)
               {
                    selectedAtomNdx = bondedAtoms.get(0);
               }
               else
               {
                    bondFound = false;
               }
          }

          selectedFragmentArray.clear();

          if (fragmentAtoms.size() > 0)
          {
               nfragmentAtoms = fragmentAtoms.size();

               rotType = getRotType();

               if (nfragmentAtoms == natoms)
               {
                    for (ndx = 0; ndx < natoms; ndx++)
                    {
                         selectedFragmentArray.add(fragmentAtoms.get(ndx));
                    }                    
               }
               else if (nfragmentAtoms < natoms)
               {
                    nfragments++;

                    // Increase the number of substructures by 1
                    nsubstructures++;

                    for (k = 0; k < nfragmentAtoms; k++)
                    {
                         fragmentAtomNdx = fragmentAtoms.get(k);     

                         coordinates[fragmentAtomNdx][4] = nfragments;
                    }
                     
                    if (rotType == 0)
                    {
                         if (nfragmentAtoms <= (int)(natoms/2))
                         {
                              for (ndx = 0; ndx < nfragmentAtoms; ndx++)
                              {
                                   selectedFragmentArray.add(fragmentAtoms.get(ndx));
                              }
                         }
                         else
                         {
                              for (ndx = 0; ndx < natoms; ndx++)
                              {
                                   if ( !fragmentAtoms.contains((int)coordinates[ndx][0]) )
                                   {
                                        selectedFragmentArray.add((int)coordinates[ndx][0]);
                                   }
                              }
                         }     
                    }
                    else if (rotType == 2)
                    {
                         for (ndx = 0; ndx < nfragmentAtoms; ndx++)
                         {
                              selectedFragmentArray.add(fragmentAtoms.get(ndx));
                         }
                    }                         
               }
          }

          return coordinates;
     }

     public ArrayList<Integer> getSelectedFragmentArray()
     {
          return selectedFragmentArray;
     }

     public ArrayList<Integer> getFragmentArray()
     {
          return fragmentAtoms;
     }

//---------------------------1

     public void deleteUserDefStructFiles()
     {
          File tmpdir, f;

          tmpdir = new File(strTmpUserDefStructureDir);

          String[] fileList = tmpdir.list();

          for (ndx = 0; ndx < fileList.length; ndx++)
          {
               f = new File(strTmpUserDefStructureDir + fileList[ndx]);

               f.delete();
          }
     }

//---------------------------1


     public void setSelectedFragments(ArrayList<Integer> selectedNames)
     {
          natoms = getNatoms();

          int ndx1, ndx2, selectedNdx, unselectedNdx, targetNdx, bondedNdx, nfrag1, nfrag2;
          int name1, name2, name3, nc; 
          int[][] fragConnectivity = new int[natoms][natoms];
          double rtx, rty, rtz, Dt, rbx, rby, rbz, Db;
          double r11, r22, r33, Dr, n11, n22, n33;
          double[][] fragmentCoords = new double[natoms][ncoordCols];
          ArrayList<Integer> fragmentAtoms = new ArrayList<Integer>();          
          ArrayList<Integer> bond1Atoms = new ArrayList<Integer>();
          ArrayList<Integer> bond2Atoms = new ArrayList<Integer>();

          atomCoordinates = getCoordinates();

          for (j = 0; j < natoms; j++)
          {
               for (k = 0; k < ncoordCols; k++)
               {
                     fragmentCoords[j][k] =  atomCoordinates[j][k];
               }
          }

          atomConnectivity = getAtomConnectivity();

          for (j = 0; j < natoms; j++)
          {
               for (k = 0; k < natoms; k++)
               {
                     fragConnectivity[j][k] =  atomConnectivity[j][k];
               }
          }

          if (selectedNames.size() == 3)
          {
               name1 = selectedNames.get(0);  name2 = selectedNames.get(1);  name3 = selectedNames.get(2);  

               if ( (name1 >= 1000000) && (name2 >= 0) && (name2 < natoms) && (name3 >= 1000000) )
               {
                    rotOriginNdx = name2;

                    ndx1 = ((name1/1000000) - 1);  ndx2 = (name1 % 1000000);

                    selectedNdx = ndx1;

                    if (ndx1 == name2)
                    {
                         selectedNdx = ndx2;
                    }
                     
                    fragConnectivity[ndx1][ndx2] = fragConnectivity[ndx2][ndx1] = 0;
            
                    ndx1 = ((name3/1000000) - 1);  ndx2 = (name3 % 1000000);

                    unselectedNdx = ndx1;

                    if (ndx1 == name2)
                    {
                         unselectedNdx = ndx2;
                    }

                    fragmentCoords = getFragmentAtoms(natoms, fragmentCoords, fragConnectivity, selectedNdx, unselectedNdx);

                    fragmentAtoms = getSelectedFragmentArray();

                    if (fragmentAtoms.size() > 0)
                    {
                         if (fragmentAtoms.contains(name2))
                         {
                              ndx = fragmentAtoms.indexOf(name2);

                              fragmentAtoms.remove(ndx);
                         }
 
                         if (fragmentAtoms.size() <= ((natoms - 1)/2))
                         {
                              selectedNdx = ndx1; 
                              unselectedNdx = ndx2;
                         }
                         else
                         {
                              selectedNdx = ndx2;
                              unselectedNdx = ndx1;
                         }

                         for (ndx = 0; ndx < natoms; ndx++)
                         {
                              if (fragmentCoords[ndx][4] == selectedNdx)
                              {
                                   selectedFragments.add(ndx);
                              }
                         }
                    }

                    // unselected index is target index, the atoms bonded to target index remain stationary during rotation 
                    rtx = atomCoordinates[unselectedNdx][1] - atomCoordinates[rotOriginNdx][1];
                    rty = atomCoordinates[unselectedNdx][2] - atomCoordinates[rotOriginNdx][2];
                    rtz = atomCoordinates[unselectedNdx][3] - atomCoordinates[rotOriginNdx][3];

                    Dt = Math.sqrt(rtx*rtx + rty*rty + rtz*rtz);

                    rtx = rtx/Dt; rty = rty/Dt; rtz = rtz/Dt;

                    // selected index is bonded index, the atoms bonded to bonded index rotate 
                    rbx = atomCoordinates[selectedNdx][1] - atomCoordinates[rotOriginNdx][1];
                    rby = atomCoordinates[selectedNdx][2] - atomCoordinates[rotOriginNdx][2];
                    rbz = atomCoordinates[selectedNdx][3] - atomCoordinates[rotOriginNdx][3];

                    Db = Math.sqrt(rbx*rbx + rby*rby + rbz*rbz);

                    rbx = rbx/Db; rby = rby/Db; rbz = rbz/Db;

                    // Compute cross product: (rt x rb)
                    r11 = rty*rbz - rtz*rby;  r22 = rtz*rbx - rtx*rbz;  r33 = rtx*rby - rty*rbx;

                    Dr = Math.sqrt(r11*r11 + r22*r22 + r33*r33);                    

                    n11 = r11/Dr; n22 = r22/Dr;  n33 = r33/Dr;

                    rotationAxis.clear();
                    rotationAxis.add(n11);  rotationAxis.add(n22);  rotationAxis.add(n33);
               }     
          }
     }

     public void rotateFragments(int attribMod, double delAngle)
     {
          int rotationOriginIndex, ndex, bvndex;
          double bondHalfAngle, X, Y, Z, rotX, rotY, rotZ, nnx, nny, nnz, bvx, bvy, bvz;
          double[][] bondRotMatrix = new double[ndimensions][ndimensions];

          rotationOriginIndex = getRotationOriginIndex();

          if (attribMod == 2)      // bond angle selected
          {
               bondAngleFragments = getBondAngleFragments();
               bondAngleFragments1 = bondAngleFragments.get(0);
               bondAngleFragments2 = bondAngleFragments.get(1);

               bondAngleRotationAxis = getBondAngleRotationAxis();
               nnx = bondAngleRotationAxis[0];  nny = bondAngleRotationAxis[1];  nnz = bondAngleRotationAxis[2];

               bondHalfAngle = 0.5*(Math.PI/180)*(delAngle);

               for (ndx = 0; ndx < bondAngleFragments1.size(); ndx++)
               {
                    indx = bondAngleFragments1.get(ndx);

                    X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                    Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                    Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                    // Compute rotation matrix
                    bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(bondHalfAngle) * (1 - nnx * nnx) );
                    bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(bondHalfAngle)) ) + (nnz * Math.sin(bondHalfAngle));
                    bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(bondHalfAngle)) ) - (nny * Math.sin(bondHalfAngle));

                    bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(bondHalfAngle)) ) - (nnz * Math.sin(bondHalfAngle));
                    bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(bondHalfAngle) * (1 - nny * nny) );
                    bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(bondHalfAngle)) ) + (nnx * Math.sin(bondHalfAngle));
  
                    bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(bondHalfAngle)) ) + (nny * Math.sin(bondHalfAngle));
                    bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(bondHalfAngle)) ) - (nnx * Math.sin(bondHalfAngle));
                    bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(bondHalfAngle) * (1 - nnz * nnz) );

                    rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                    rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                    rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                    atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                    atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                    atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                    for (ndex = 0; ndex < bondVectors.length; ndex++)
                    {
                         bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                         if (bvndex == indx)
                         {
                              bvx = bondVectors[ndex][1];  bvy = bondVectors[ndex][2];  bvz = bondVectors[ndex][3];  

                              bondVectors[ndex][1] = bondRotMatrix[0][0]*bvx + bondRotMatrix[0][1]*bvy + bondRotMatrix[0][2]*bvz;
                              bondVectors[ndex][2] = bondRotMatrix[1][0]*bvx + bondRotMatrix[1][1]*bvy + bondRotMatrix[1][2]*bvz;
                              bondVectors[ndex][3] = bondRotMatrix[2][0]*bvx + bondRotMatrix[2][1]*bvy + bondRotMatrix[2][2]*bvz;
                         }
                    }
               }

               for (ndx = 0; ndx < bondAngleFragments2.size(); ndx++)
               {
                    indx = bondAngleFragments2.get(ndx);

                    X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                    Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                    Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                    // Compute rotation matrix
                    bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(-bondHalfAngle) * (1 - nnx * nnx) );
                    bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(-bondHalfAngle)) ) + (nnz * Math.sin(-bondHalfAngle));
                    bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(-bondHalfAngle)) ) - (nny * Math.sin(-bondHalfAngle));

                    bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(-bondHalfAngle)) ) - (nnz * Math.sin(-bondHalfAngle));
                    bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(-bondHalfAngle) * (1 - nny * nny) );
                    bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(-bondHalfAngle)) ) + (nnx * Math.sin(-bondHalfAngle));
  
                    bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(-bondHalfAngle)) ) + (nny * Math.sin(-bondHalfAngle));
                    bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(-bondHalfAngle)) ) - (nnx * Math.sin(-bondHalfAngle));
                    bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(-bondHalfAngle) * (1 - nnz * nnz) );

                    rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                    rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                    rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                    atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                    atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                    atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                    for (ndex = 0; ndex < bondVectors.length; ndex++)
                    {
                         bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                         if (bvndex == indx)
                         {
                              bvx = bondVectors[ndex][1];  bvy = bondVectors[ndex][2];  bvz = bondVectors[ndex][3];  

                              bondVectors[ndex][1] = bondRotMatrix[0][0]*bvx + bondRotMatrix[0][1]*bvy + bondRotMatrix[0][2]*bvz;
                              bondVectors[ndex][2] = bondRotMatrix[1][0]*bvx + bondRotMatrix[1][1]*bvy + bondRotMatrix[1][2]*bvz;
                              bondVectors[ndex][3] = bondRotMatrix[2][0]*bvx + bondRotMatrix[2][1]*bvy + bondRotMatrix[2][2]*bvz;
                         }
                    }
               }
          }
          else if (attribMod == 3)      // dihedral angle selected 
          {
               dihedralAngleFragments = getDihedralAngleFragments();

               dihedralAngleRotationAxis = getDihedralAngleRotationAxis();

               nnx = dihedralAngleRotationAxis[0];  nny = dihedralAngleRotationAxis[1];  nnz = dihedralAngleRotationAxis[2];

               delDihedralAngle = (Math.PI/180)*delAngle;

               for (ndx = 0; ndx < dihedralAngleFragments.size(); ndx++)
               {
                    indx = dihedralAngleFragments.get(ndx);

                    X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                    Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                    Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                    // Compute rotation matrix
                    bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(delDihedralAngle) * (1 - nnx * nnx) );
                    bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(delDihedralAngle)) ) + (nnz * Math.sin(delDihedralAngle));
                    bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(delDihedralAngle)) ) - (nny * Math.sin(delDihedralAngle));

                    bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(delDihedralAngle)) ) - (nnz * Math.sin(delDihedralAngle));
                    bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(delDihedralAngle) * (1 - nny * nny) );
                    bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(delDihedralAngle)) ) + (nnx * Math.sin(delDihedralAngle));
  
                    bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(delDihedralAngle)) ) + (nny * Math.sin(delDihedralAngle));
                    bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(delDihedralAngle)) ) - (nnx * Math.sin(delDihedralAngle));
                    bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(delDihedralAngle) * (1 - nnz * nnz) );

                    rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                    rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                    rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                    atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                    atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                    atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                    for (ndex = 0; ndex < bondVectors.length; ndex++)
                    {
                         bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                         if (bvndex == indx)
                         {
                              bvx = bondVectors[ndex][1];  bvy = bondVectors[ndex][2];  bvz = bondVectors[ndex][3];  

                              bondVectors[ndex][1] = bondRotMatrix[0][0]*bvx + bondRotMatrix[0][1]*bvy + bondRotMatrix[0][2]*bvz;
                              bondVectors[ndex][2] = bondRotMatrix[1][0]*bvx + bondRotMatrix[1][1]*bvy + bondRotMatrix[1][2]*bvz;
                              bondVectors[ndex][3] = bondRotMatrix[2][0]*bvx + bondRotMatrix[2][1]*bvy + bondRotMatrix[2][2]*bvz;
                         }
                    }
               }
          }
          else if (attribMod == 4)      // axial rotation about bond 
          {
               axialFragmentAtoms = getAxialFragmentAtoms();

               rotationOriginIndex = getRotationOriginIndex();

               axialRotationAxis = getAxialRotationAxis();

               nnx = axialRotationAxis[0];  nny = axialRotationAxis[1];  nnz = axialRotationAxis[2];

               delAxialAngle = delAngle;

               for (ndx = 0; ndx < axialFragmentAtoms.size(); ndx++)
               {
                    indx = axialFragmentAtoms.get(ndx);

                    X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                    Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                    Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                    // Compute rotation matrix
                    bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(delAxialAngle) * (1 - nnx * nnx) );
                    bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) + (nnz * Math.sin(delAxialAngle));
                    bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) - (nny * Math.sin(delAxialAngle));

                    bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) - (nnz * Math.sin(delAxialAngle));
                    bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(delAxialAngle) * (1 - nny * nny) );
                    bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) + (nnx * Math.sin(delAxialAngle));
  
                    bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) + (nny * Math.sin(delAxialAngle));
                    bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) - (nnx * Math.sin(delAxialAngle));
                    bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(delAxialAngle) * (1 - nnz * nnz) );

                    rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                    rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                    rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                    atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                    atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                    atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                    for (ndex = 0; ndex < bondVectors.length; ndex++)
                    {
                         bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                         if (bvndex == indx)
                         {
                              bvx = bondVectors[ndex][1];  bvy = bondVectors[ndex][2];  bvz = bondVectors[ndex][3];  

                              bondVectors[ndex][1] = bondRotMatrix[0][0]*bvx + bondRotMatrix[0][1]*bvy + bondRotMatrix[0][2]*bvz;
                              bondVectors[ndex][2] = bondRotMatrix[1][0]*bvx + bondRotMatrix[1][1]*bvy + bondRotMatrix[1][2]*bvz;
                              bondVectors[ndex][3] = bondRotMatrix[2][0]*bvx + bondRotMatrix[2][1]*bvy + bondRotMatrix[2][2]*bvz;
                         }
                    }
               }
          }
                                
          glcanvas.repaint();
     }


//-------------------------------2

     public double[][] combineFragments(int fragmentID, double[] molAxis, double[] fragAxis, int molAtomNdx, int fragAtomNdx)
     {
          int index, molElementNdx, fragElementNdx;
          double mbvx, mbvy, mbvz, fbvx, fbvy, fbvz;
          double nx, ny, nz, n11, n22, n33, R;
          double angle, cosangle, sinangle, paraltol, fragRotationAngle;
          double fragx, fragy, fragz, fragx_star, fragy_star, fragz_star;
          double bvx, bvy, bvz, bvx_star, bvy_star, bvz_star;
          double delX, delY, delZ;     

          double[][] rotMatrix = new double[ncoords][ncoords];
          double[][] coordinates = new double[natoms][ncoordCols];

          String symbol;

          boolean axesAntiparallel = false;

          mbvx = molAxis[0];  mbvy = molAxis[1];  mbvz = molAxis[2];     
          fbvx = fragAxis[0];  fbvy = fragAxis[1];  fbvz = fragAxis[2];     

          n11 = n22 = n33 = 0.0;  

          molElementNdx = fragElementNdx = 0;

          // Get element index for both the selected molecule atom and the selected fragment atom
          symbol = elemID.get(molAtomNdx);

          for (ndx = 0; ndx < atomicSymbol.length; ndx++)
          {
               if (atomicSymbol[ndx].equals(symbol))
               {
                    molElementNdx = ndx;
                    break;
               }
          }

          symbol = elemID.get(fragAtomNdx);

          for (ndx = 0; ndx < atomicSymbol.length; ndx++)
          {
               if (atomicSymbol[ndx].equals(symbol))
               {
                    fragElementNdx = ndx;

                    break;
               }
          }

          bondOrder = atomConnectivity[molAtomNdx][fragAtomNdx];

          defaultBondLength = getDefaultBondLength(molElementNdx, fragElementNdx, bondOrder);

          // If fragAxis is parallel to molAxis, rotate entire fragment 180 degrees about normal to fragAxis
          cosangle = mbvx*fbvx + mbvy*fbvy + mbvz*fbvz;
          sinangle = Math.sin(Math.acos(cosangle));

          angle = (180/Math.PI)*Math.acos(cosangle);

          if (cosangle == (-1))
          {
               axesAntiparallel = true;

               // do nothing since fragment bond vector is already anti-|| to molecule bond vector 
          }
          else if (Math.abs(angle) <= 0.5)     // fragment bond vector is || to molecule bond vector => it must be rotated to anti-|| orientation
          {
               // Compute a normal to fragAxis: construct an arbitrary vector that is not || to fragAxis and compute its cross-product with fragAxis
               nx = fbvx + ( (2*fbvy) + (3*fbvz) );   ny = fbvy + ( (3*fbvx) + (4*fbvz) );  nz = fbvz + ( (4*fbvx) + (5*fbvy) ); 
 
               R = Math.sqrt((nx*nx) + (ny*ny) + (nz*nz));
 
               nx = nx/R;  ny = ny/R;  nz = nz/R;

               n11 = ny*fbvz - nz*fbvy;  n22 = nz*fbvx - nx*fbvz;  n33 = nx*fbvy - ny*fbvx;       

               R = Math.sqrt((n11*n11) + (n22*n22) + (n33*n33));

               n11 = n11/R;  n22 = n22/R;  n33 = n33/R;
          
               // Rotate 180 about axis normal to fragAxis
               cosangle = (-1.0);
               sinangle = 0.0;
          }
          else
          {
               // Compute normal to fragAxis and (-molAxis):  fragAxis x (-molAxis) 

               // Reverse direction of molAxis
               mbvx = -mbvx;  mbvy = -mbvy;  mbvz = -mbvz;

               n11 = mbvy*fbvz - mbvz*fbvy;  n22 = mbvz*fbvx - mbvx*fbvz;  n33 = mbvx*fbvy - mbvy*fbvx;      

               cosangle = mbvx*fbvx + mbvy*fbvy + mbvz*fbvz;
               sinangle = Math.sin(Math.acos(cosangle));
          }

          // Rotate fragment atoms and fragment bond vectors counterclockwise about (n11, n22, n33) if fragAxis is not || to molAxis

          if (!axesAntiparallel)
          {
               rotMatrix[0][0] = (n11 * n11) + (cosangle * (1 - n11 * n11));
               rotMatrix[0][1] = ((n11 * n22) * (1 - cosangle)) + (n33 * sinangle);
               rotMatrix[0][2] = ((n33 * n11) * (1 - cosangle)) - (n22 * sinangle);

               rotMatrix[1][0] = ((n11 * n22) * (1 - cosangle)) - (n33 * sinangle);
               rotMatrix[1][1] = (n22 * n22) + (cosangle * (1 - n22 * n22));
               rotMatrix[1][2] = ((n22 * n33) * (1 - cosangle)) + (n11 * sinangle);
 
               rotMatrix[2][0] = ((n33 * n11) * (1 - cosangle)) + (n22 * sinangle);
               rotMatrix[2][1] = ((n22 * n33) * (1 - cosangle)) - (n11 * sinangle);
               rotMatrix[2][2] = (n33 * n33) + (cosangle * (1 - n33 * n33));

               // Rotate fragment coordinates
               for (ndx = 0; ndx < natoms; ndx++)
               {
                    if (atomCoordinates[ndx][4] == fragmentID)
                    {
                         fragx = atomCoordinates[ndx][1] - atomCoordinates[fragAtomNdx][1];  
                         fragy = atomCoordinates[ndx][2] - atomCoordinates[fragAtomNdx][2];  
                         fragz = atomCoordinates[ndx][3] - atomCoordinates[fragAtomNdx][3];

                         fragx_star = rotMatrix[0][0] * fragx + rotMatrix[0][1] * fragy + rotMatrix[0][2] * fragz; 
                         fragy_star = rotMatrix[1][0] * fragx + rotMatrix[1][1] * fragy + rotMatrix[1][2] * fragz; 
                         fragz_star = rotMatrix[2][0] * fragx + rotMatrix[2][1] * fragy + rotMatrix[2][2] * fragz; 

                         atomCoordinates[ndx][1] = fragx_star + atomCoordinates[fragAtomNdx][1];  
                         atomCoordinates[ndx][2] = fragy_star + atomCoordinates[fragAtomNdx][2];  
                         atomCoordinates[ndx][3] = fragz_star + atomCoordinates[fragAtomNdx][3];
                    }
               }

               // Rotate fragment bond vectors
               for (ndx = 0; ndx < bondVectors.length; ndx++)
               {
                    index = ( -((int)(bondVectors[ndx][0]/100) + 1) );

                    if (atomCoordinates[index][4] == fragmentID)
                    {
                         bvx = bondVectors[ndx][1];  bvy = bondVectors[ndx][2]; bvz = bondVectors[ndx][3]; 

                         bvx_star = rotMatrix[0][0] * bvx + rotMatrix[0][1] * bvy + rotMatrix[0][2] * bvz;  
                         bvy_star = rotMatrix[1][0] * bvx + rotMatrix[1][1] * bvy + rotMatrix[1][2] * bvz;  
                         bvz_star = rotMatrix[2][0] * bvx + rotMatrix[2][1] * bvy + rotMatrix[2][2] * bvz; 

                         bondVectors[ndx][1] = bvx_star;  bondVectors[ndx][2] = bvy_star;  bondVectors[ndx][3] = bvz_star;
                    }
               }

               // Reverse direction of molAxis back to original orientation
               mbvx = -mbvx;  mbvy = -mbvy;  mbvz = -mbvz;

               // Translate fragment atoms along molAxis to a position a distance 'defaultBondLength' from atom to which fragment is being bonded
               delX = atomCoordinates[fragAtomNdx][1] - atomCoordinates[molAtomNdx][1];
               delY = atomCoordinates[fragAtomNdx][2] - atomCoordinates[molAtomNdx][2];
               delZ = atomCoordinates[fragAtomNdx][3] - atomCoordinates[molAtomNdx][3];

               for (ndx = 0; ndx < natoms; ndx++)
               {
                    if (atomCoordinates[ndx][4] == fragmentID)
                    {
                         atomCoordinates[ndx][1] += (-delX + (defaultBondLength * mbvx));  
                         atomCoordinates[ndx][2] += (-delY + (defaultBondLength * mbvy));  
                         atomCoordinates[ndx][3] += (-delZ + (defaultBondLength * mbvz));  
                    }
               } 
          }

          return atomCoordinates;
     }

//-------------------------------2



     public void addHydrogens()
     {
          int targetNdx, bondedNdx, numbvs, nnonpassivatedBVs;
          double delx, dely, delz;

          int[][] tmpConnectivity;
          double[][] tmpCoordinates;
          double[][] tmpBondVecs;
          float[][] tmpRGB;

          natoms = getNatoms();
          elemID = getElemID();

          bondVectors = getBondVectors();

          numbvs = bondVectors.length;
          nbvRows = 0;

          for (ndx = 0; ndx < numbvs; ndx++)
          {
               if ((int)bondVectors[ndx][4] == 1)
               {
                    nbvRows++;
               }
          }

          nnonpassivatedBVs = numbvs - nbvRows;

          bondOrder = 1;

          if (nbvRows > 0)
          {
               tmpCoordinates = getCoordinates();
               atomCoordinates = new double[(natoms + nbvRows)][ncoordCols];

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < ncoordCols; k++)
                    {               
                         atomCoordinates[j][k] = tmpCoordinates[j][k];
                    }
               }

               tmpRGB = getElementRGB();
               elementRGB = new float[(natoms + nbvRows)][ncolors];

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < ncolors; k++)
                    {               
                         elementRGB[j][k] = tmpRGB[j][k];
                    }
               }

               tmpConnectivity = getAtomConnectivity();
               atomConnectivity = new int[(natoms + nbvRows)][(natoms + nbvRows)];

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < natoms; k++)
                    {                                             
                         atomConnectivity[j][k] = tmpConnectivity[j][k];
                    }
               }

               for (ndx = 0; ndx < nbvRows; ndx++)
               {
                    elemID.add("H");
     
                    for (k = 0; k < ncolors; k++)
                    {                                
                         elementRGB[(natoms + ndx)][k] = 1.0f;
                    }
                
                    targetNdx = (-((int)(bondVectors[ndx][0]/100) + 1));
                    bondedNdx = (natoms + ndx);

                    atomConnectivity[targetNdx][bondedNdx] = atomConnectivity[bondedNdx][targetNdx] = 1;
                    nbonds++;

                    defaultBondLength = getDefaultBondLength(5, 0, bondOrder);

                    atomCoordinates[(natoms + ndx)][0] = 0;
                    atomCoordinates[(natoms + ndx)][4] = atomCoordinates[targetNdx][4];

                    for (k = 0; k < ncoords; k++)
                    {                         
                         atomCoordinates[(natoms + ndx)][(k + 1)] = atomCoordinates[targetNdx][(k + 1)] + defaultBondLength*bondVectors[ndx][(k + 1)]; 
                    }
               }

               natoms += nbvRows;

               AN = getAN(elemID, natoms);

               atomRadii = getAtomRadii(AN);

               tmpBondVecs = new double[numbvs][nbvCols];

               for (j = 0; j < numbvs; j++)
               {
                    for (k = 0; k < nbvCols; k++)
                    {   
                         tmpBondVecs[j][k] = bondVectors[j][k];
                    }
               }

               bondVectors = new double[nnonpassivatedBVs][nbvCols];

               ndx = (-1);

               for (j = 0; j < numbvs; j++)
               {
                    if ((int)tmpBondVecs[j][4] != 1)
                    { 
                         ndx++;

                         for (k = 0; k < nbvCols; k++)
                         {   
                              bondVectors[ndx][k] = tmpBondVecs[j][k];
                         }
                    }
               }

               atomNames.clear();
               setAtomNames(elemID);

               atomNames = getAtomNames();
          }

          generateMol2File(moleculeName);

          glcanvas.repaint();
     }

     public ArrayList<Integer> getSelectedFragments()
     {
          return selectedFragments;
     }

     public ArrayList<Double> getRotationAxis()
     {
          return rotationAxis;
     }

     public String getDisplayedFile()
     {
          return displayedFile;
     }  

     public String getElementSelected()
     {
          return elementSelected;
     }

     public ArrayList<String> getElemID()
     {
          return elemID;
     }

     public ArrayList<String> getFragElemID()
     {
          return fragElemID;
     }

     public ArrayList<Integer> getAN(ArrayList<String> elemID, int natoms)
     {
          String elem;

          AN.clear();

          for (j = 0; j < natoms; j++)
          {
               elem = elemID.get(j);

               for (k = 0; k < atomicSymbol.length; k++)
               {
                    if (atomicSymbol[k].equals(elem))                   
                    {
                         AN.add((k + 1));
                         break;
                    }
               }
          }

          return AN;
     }    

     public int[] getAtomicNumber(String[] atomID, int natoms)
     {
          int[] atomicNumber = new int[natoms];

          for (j = 0; j < natoms; j++)
          {
               for (k = 0; k < atomicSymbol.length; k++)
               {
                    if (atomicSymbol[k].equals(atomID[j]))
                    {
                         atomicNumber[j] = (k + 1);
                         break;
                    }
               }
          }

          return atomicNumber;
     }             


//-------------------------3


     public double getDefaultBondLength(int targetElementNdx, int bondedElementNdx, int bondOrder)
     {
          int AN1, AN2;
          double bl;
          double[ ] covrad  =  {0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00};         

          AN1 = AN2 = 0;
          bl = 0;                                 // initialize bond length
          defaultBondLength = 0;

          if (targetElementNdx <= bondedElementNdx)
          {
               AN1 = targetElementNdx + 1;          // atomic number of target atom
               AN2 = bondedElementNdx + 1;          // atomic number of bonded atom
          }
          else if (targetElementNdx > bondedElementNdx)
          {
               AN1 = bondedElementNdx + 1;          // order atomic numbers from lower to higher
               AN2 = targetElementNdx + 1;    
          }

          switch(AN1)
          {
               case 1: switch(AN2)
                       {
                            case 1: bl = 0.74;     // H-H
                                    break;   
                            case 6: bl = 1.10;     // H-C
                                    break;   
                            case 7: bl = 1.00;     // H-N
                                    break;   
                            case 8: bl = 0.97;     // H-O
                                    break;   
                            case 9: bl = 0.92;     // H-F
                                    break;   
                           case 16: bl = 1.32;     // H-S
                                    break;   
                           case 17: bl = 1.27;     // H-Cl
                                    break;   
                           case 35: bl = 1.41;     // H-Br
                                    break;   
                           case 53: bl = 1.61;     // H-I
                                    break;   
                       }
                       break;
               case 6: switch(AN2)
                       {
                            case 6: if (bondOrder == 1)
                                    {
                                         bl = 1.54;   // C-C
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.34;   // C=C
                                    }
                                    else if (bondOrder == 3)
                                    {
                                         bl = 1.20;   // triple bond
                                    }
                                    break;
                            case 7: if (bondOrder == 1)
                                    {
                                         bl = 1.47;   // C-N
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.28;   // C=N
                                    }
                                    else if (bondOrder == 3)
                                    {
                                         bl = 1.16;   // triple bond
                                    }
                                    break;
                            case 8: if (bondOrder == 1)
                                    {
                                         bl = 1.43;   // C-O
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.20;   // C=O
                                    }
                                    break;
                           case 17: bl = 1.78; break;   // C-Cl
                       }
                       break;
               case 7: switch(AN2)
                       {
                            case 7: if (bondOrder == 1)
                                    {
                                         bl = 1.45;   // N-N
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.23;   // N=N
                                    }
                                    else if (bondOrder == 3)
                                    {
                                         bl = 1.10;   // triple bond
                                    }
                                    break;
                            case 8: if (bondOrder == 1)
                                    {
                                         bl = 1.36;   // N-O
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.20;   // N=O
                                    }
                                    break;
                       }
                       break;
               case 8: switch(AN2)
                       {
                            case 8: if (bondOrder == 1)
                                    {
                                         bl = 1.45;   // O-O
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.21;   // O=O
                                    }
                                    break;
                       }
                       break;
               case 9: switch(AN2)
                       {
                            case 9: bl = 1.43; break;   // F-F
                       }
                       break;
              case 17: switch(AN2)
                       {
                           case 17: bl = 1.99; break;   // Cl-Cl
                       }
                       break;
              case 35: switch(AN2)
                       {
                           case 35: bl = 2.28; break;   // Br-Br
                       }
                       break;
              case 53: switch(AN2)
                       {
                           case 53: bl = 2.66; break;   // I-I
                       }
                       break;
          }

          if (bl > 0)
          {
               defaultBondLength = bl;
          }
          else
          {
               defaultBondLength = covrad[(AN1 - 1)] + covrad[(AN2 - 1)];
          }

          return defaultBondLength;
     }


/*
     public double getDefaultBondLength(int targetAtomNdx, int bondedAtomNdx, int bondOrder)
     {
          int AN1, AN2;
          double bl;
          double[ ] covrad  =  {0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00};         

          AN1 = AN2 = 0;
          bl = 0;                                 // initialize bond length
          defaultBondLength = 0;

          if (targetAtomNdx <= bondedAtomNdx)
          {
               AN1 = targetAtomNdx + 1;          // atomic number of target atom
               AN2 = bondedAtomNdx + 1;          // atomic number of bonded atom
          }
          else if (targetAtomNdx > bondedAtomNdx)
          {
               AN1 = bondedAtomNdx + 1;          // order atomic numbers from lower to higher
               AN2 = targetAtomNdx + 1;    
          }

          switch(AN1)
          {
               case 1: switch(AN2)
                       {
                            case 1: bl = 0.74;     // H-H
                                    break;   
                            case 6: bl = 1.10;     // H-C
                                    break;   
                            case 7: bl = 1.00;     // H-N
                                    break;   
                            case 8: bl = 0.97;     // H-O
                                    break;   
                            case 9: bl = 0.92;     // H-F
                                    break;   
                           case 16: bl = 1.32;     // H-S
                                    break;   
                           case 17: bl = 1.27;     // H-Cl
                                    break;   
                           case 35: bl = 1.41;     // H-Br
                                    break;   
                           case 53: bl = 1.61;     // H-I
                                    break;   
                       }
                       break;
               case 6: switch(AN2)
                       {
                            case 6: if (bondOrder == 1)
                                    {
                                         bl = 1.54;   // C-C
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.34;   // C=C
                                    }
                                    else if (bondOrder == 3)
                                    {
                                         bl = 1.20;   // triple bond
                                    }
                                    break;
                            case 7: if (bondOrder == 1)
                                    {
                                         bl = 1.47;   // C-N
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.28;   // C=N
                                    }
                                    else if (bondOrder == 3)
                                    {
                                         bl = 1.16;   // triple bond
                                    }
                                    break;
                            case 8: if (bondOrder == 1)
                                    {
                                         bl = 1.43;   // C-O
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.20;   // C=O
                                    }
                                    break;
                           case 17: bl = 1.78; break;   // C-Cl
                       }
                       break;
               case 7: switch(AN2)
                       {
                            case 7: if (bondOrder == 1)
                                    {
                                         bl = 1.45;   // N-N
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.23;   // N=N
                                    }
                                    else if (bondOrder == 3)
                                    {
                                         bl = 1.10;   // triple bond
                                    }
                                    break;
                            case 8: if (bondOrder == 1)
                                    {
                                         bl = 1.36;   // N-O
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.20;   // N=O
                                    }
                                    break;
                       }
                       break;
               case 8: switch(AN2)
                       {
                            case 8: if (bondOrder == 1)
                                    {
                                         bl = 1.45;   // O-O
                                    }
                                    else if (bondOrder == 2)
                                    {
                                         bl = 1.21;   // O=O
                                    }
                                    break;
                       }
                       break;
               case 9: switch(AN2)
                       {
                            case 9: bl = 1.43; break;   // F-F
                       }
                       break;
              case 17: switch(AN2)
                       {
                           case 17: bl = 1.99; break;   // Cl-Cl
                       }
                       break;
              case 35: switch(AN2)
                       {
                           case 35: bl = 2.28; break;   // Br-Br
                       }
                       break;
              case 53: switch(AN2)
                       {
                           case 53: bl = 2.66; break;   // I-I
                       }
                       break;
          }

          if (bl > 0)
          {
               defaultBondLength = bl;
          }
          else
          {
               defaultBondLength = covrad[(AN1 - 1)] + covrad[(AN2 - 1)];
          }

          return defaultBondLength;
     }
*/

//-------------------------3


     public double[][] getNewBondVectors(int selectedAtomNdx, int nbondvectors)
     {
          int atmnmbr;
          double[][] bndvec = new double[nbondvectors][(ncoords+1)];

          newBondVectors = new double[nbondVectors][nnewbvCols];

          atmnmbr = selectedAtomNdx + 1;

          switch(atmnmbr)
          {
               case 1: // H
                       bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                       break;
               case 4: // Be
                       bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                       bndvec[1][0] = -0.66961; bndvec[1][1] = -0.74272; bndvec[1][2] = 0.0; bndvec[1][3] = 1;
                       break;
               case 5: // B
                       switch(nbondvectors)
                       {
                            case 3: bndvec[0][0] =  0.61391; bndvec[0][1] = -0.78269; bndvec[0][2] = -0.10249; bndvec[0][3] = 1;
                                    bndvec[1][0] =  0.35634; bndvec[1][1] =  0.93287; bndvec[1][2] =  0.05271; bndvec[1][3] = 1;
                                    bndvec[2][0] = -0.99243; bndvec[2][1] = -0.12069; bndvec[2][2] =  0.02253; bndvec[2][3] = 1;
                                    break;
                       }
                       break;
               case 6: // C
                       switch(nbondvectors)
                       {
                            case 2: bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                                    bndvec[1][0] = -0.66961; bndvec[1][1] = -0.74272; bndvec[1][2] = 0.0; bndvec[1][3] = 3;
                                    break;
                            case 3: bndvec[0][0] =  0.61391; bndvec[0][1] = -0.78269; bndvec[0][2] = -0.10249; bndvec[0][3] = 1;
                                    bndvec[1][0] =  0.35634; bndvec[1][1] =  0.93287; bndvec[1][2] =  0.05271; bndvec[1][3] = 1;
                                    bndvec[2][0] = -0.99243; bndvec[2][1] = -0.12069; bndvec[2][2] =  0.02253; bndvec[2][3] = 2;
                                    break;
                            case 4: bndvec[0][0] = -0.07084; bndvec[0][1] =  0.94533; bndvec[0][2] =  0.31834; bndvec[0][3] = 1;
                                    bndvec[1][0] =  0.96390; bndvec[1][1] = -0.24983; bndvec[1][2] = -0.09208; bndvec[1][3] = 1;
                                    bndvec[2][0] = -0.43667; bndvec[2][1] = -0.61492; bndvec[2][2] =  0.65665; bndvec[2][3] = 1;
                                    bndvec[3][0] = -0.45427; bndvec[3][1] = -0.08864; bndvec[3][2] = -0.88644; bndvec[3][3] = 1;
                                    break;
                       }
                       break;
               case 7: // N
                       switch(nbondvectors)
                       {
                            case 1: bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 3;
                                    break;              
                            case 2: bndvec[0][0] = 0.86588; bndvec[0][1] =  0.49873; bndvec[0][2] =  0.03899; bndvec[0][3] = 1;
                                    bndvec[1][0] = 0.00017; bndvec[1][1] = -0.99612; bndvec[1][2] = -0.08801; bndvec[1][3] = 2;
                                    break;
                            case 3: if (hybridization.equals("sp3"))
                                    {
                                         bndvec[0][0] =  0.96657; bndvec[0][1] =  0.01364; bndvec[0][2] =  0.25602; bndvec[0][3] = 1;
                                         bndvec[1][0] = -0.27402; bndvec[1][1] = -0.93636; bndvec[1][2] = -0.21941; bndvec[1][3] = 1;
                                         bndvec[2][0] = -0.55198; bndvec[2][1] =  0.33844; bndvec[2][2] =  0.76208; bndvec[2][3] = 1;
                                    }
                                    else if (hybridization.equals("trigonalPlanar"))
                                    {
                                         bndvec[0][0] =  0.866025; bndvec[0][1] =  0.50; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                                         bndvec[1][0] = -0.866025; bndvec[1][1] =  0.50; bndvec[1][2] = 0.0; bndvec[1][3] = 1;
                                         bndvec[2][0] =  0.0;      bndvec[2][1] = -1.00;  bndvec[2][2] = 0.0; bndvec[2][3] = 1;
                                    }
                                    break;
                       }
                       break;
               case 8: // O
                       switch(nbondvectors)
                       {
                            case 1: bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 2;
                                    break;
                            case 2: bndvec[0][0] =  0.96657; bndvec[0][1] =  0.01364; bndvec[0][2] =  0.25602; bndvec[0][3] = 1;
                                    bndvec[1][0] = -0.27402; bndvec[1][1] = -0.93636; bndvec[1][2] = -0.21941; bndvec[1][3] = 1;
                                    break;
                       }
                       break;
               case 9: // F
                       bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                       break;

              case 15: // P
                       switch(nbondvectors)
                       {
                            case 5: bndvec[0][0] =  1.0;  bndvec[0][1] =  0.0; bndvec[0][2] =  0.0;    bndvec[0][3] = 1;
                                    bndvec[1][0] = -0.50; bndvec[1][1] =  0.0; bndvec[1][2] =  0.8660; bndvec[1][3] = 1;
                                    bndvec[2][0] = -0.50; bndvec[2][1] =  0.0; bndvec[2][2] = -0.8660; bndvec[2][3] = 1;
                                    bndvec[3][0] =  0.0;  bndvec[3][1] =  1.0; bndvec[3][2] =  0.0;    bndvec[3][3] = 1; 
                                    bndvec[4][0] =  0.0;  bndvec[4][1] = -1.0; bndvec[4][2] =  0.0;    bndvec[4][3] = 1; 
                                    break;
                       }
                       break;
              case 16: // S
                       switch(nbondvectors)
                       {
                            case 2: bndvec[0][0] = 0.66961; bndvec[0][1] =  0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                                    bndvec[1][0] = 0.71373; bndvec[1][1] = -0.70042; bndvec[1][2] = 0.0; bndvec[1][3] = 1;
                                    break;
                            case 4: bndvec[0][0] = 0.50; bndvec[0][1] = 0.0;  bndvec[0][2] = 0.866025;  bndvec[0][3] = 1; //bndvec[0][4] = 5; 
                                    bndvec[1][0] = 0.50; bndvec[1][1] = 0.0;  bndvec[1][2] = -0.866025; bndvec[1][3] = 1; //bndvec[1][4] = 5; 
                                    bndvec[2][0] = 0.0;  bndvec[2][1] = 1.0;  bndvec[2][2] = 0.0;       bndvec[2][3] = 1; //bndvec[2][4] = 5; 
                                    bndvec[3][0] = 0.0;  bndvec[3][1] = -1.0; bndvec[3][2] = 0.0;       bndvec[3][3] = 1; //bndvec[3][4] = 5; 
                                    break;
                            case 6: bndvec[0][0] = 0.70711;  bndvec[0][1] = 0.0;  bndvec[0][2] = 0.70711;  bndvec[0][3] = 1; //bndvec[0][4] = 6; 
                                    bndvec[1][0] = 0.70711;  bndvec[1][1] = 0.0;  bndvec[1][2] = -0.70711; bndvec[1][3] = 1; //bndvec[1][4] = 6; 
                                    bndvec[2][0] = -0.70711; bndvec[2][1] = 0.0;  bndvec[2][2] = 0.70711;  bndvec[2][3] = 1; //bndvec[2][4] = 6; 
                                    bndvec[3][0] = -0.70711; bndvec[3][1] = 0.0;  bndvec[3][2] = -0.70711; bndvec[3][3] = 1; //bndvec[3][4] = 6; 
                                    bndvec[4][0] = 0.0;      bndvec[4][1] = 1.0;  bndvec[4][2] = 0.0;      bndvec[4][3] = 1; //bndvec[4][4] = 6; 
                                    bndvec[5][0] = 0.0;      bndvec[5][1] = -1.0; bndvec[5][2] = 0.0;      bndvec[5][3] = 1; //bndvec[5][4] = 6; 
                                    break;
                       }
                       break;
              case 17: // Cl
                       bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                       break;
              case 35: // Br
                       bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                       break;
              case 53: // I
                       bndvec[0][0] = 0.66961; bndvec[0][1] = 0.74272; bndvec[0][2] = 0.0; bndvec[0][3] = 1;
                       break;
          }                  

          for (j = 0; j < nbondvectors; j++)
          {
               for (k = 0; k < 4; k++)
               {
                   newBondVectors[j][k] = bndvec[j][k];
               }
          }

          return newBondVectors;
     } 

     public int getNumMouseClicks()
     {
         return numMouseClicks;
     }


/////////////////////////

     public void readcgenffOutfile(String infilename)
     {



          int nameNdx;
          double Q;

          String atomid, input, lineElem1, lineElem2;
          Scanner fileScan, lineScan;

          File file;
          file = new File(infilename);

          natoms = getNatoms();
          atomNames = getAtomNames();

          atomTypes.clear();  

          for (j = 0; j < natoms; j++)
          {
               atomTypes.add("N/A");
               atomCharges.add(0.0);
          }

          try
          {
               fileScan = new Scanner(file);

               input = (fileScan.nextLine()).trim();
               
               while (fileScan.hasNextLine())
               {
                    input = (fileScan.nextLine()).trim();


// System.out.println(input);


                    if (input.startsWith("GROUP"))
                    {
                         for (k = 0; k < natoms; k++)
                         {
                              input = fileScan.nextLine();

                              lineScan = new Scanner(input);

                              lineElem1 = lineScan.next();
                              atomid = lineScan.next();

                              nameNdx = atomNames.indexOf(atomid);

                              atomTypes.set(nameNdx, lineScan.next());

                              Q = Double.parseDouble(lineScan.next());
                              atomCharges.set(nameNdx, Q); 


// System.out.println(atomTypes.get(k) + "    " + Q);


                              lineScan.close();
                         }
                    }
               }                    

               fileScan.close();

               file.delete();
          }
          catch (FileNotFoundException ex)
          {

          }







     }

/////////////////////////

//     public void runcgenffexe(String infilename)
     public void generateAtomTypes(String infilename)
     {
          String batFilename = new String();
          String batFileTxt = new String();

          BufferedWriter outfile;

          Process p = null;

          File batFile;
          batFilename = "cgenff_cmd.bat";
          batFileTxt = cgenffDir + "cgenff.exe -v -v " +  infilename + " > " + "cgenff_out.txt" + " 2>&1";

          try
          {
               outfile = new BufferedWriter(new FileWriter(batFilename));

               outfile.write(batFileTxt);

               outfile.close();
          }
          catch (IOException e)
          {
               e.printStackTrace();
          }

          try
          {
               p = Runtime.getRuntime().exec(batFilename);
               p.waitFor();

               p.destroy();

               batFile = new File(batFilename);
               batFile.delete();
          }
          catch (Exception e)
          {
               e.printStackTrace();
          }
     }




public ArrayList<String> concatenateWithPenalty(ArrayList<String> arrlist1, ArrayList<String> arrlist2)
{
     int len1, len2;
     String str1, str2;

     ArrayList<String> concatSrings = new ArrayList<String>();

     len1 = arrlist1.size(); len2 = arrlist2.size(); 

     if (len1 == len2)
     {
          for(k = 0; k < len1; k++)
          {
               str1 = (arrlist1.get(k)).trim();
               str2 = (arrlist2.get(k)).trim();
     
               concatSrings.add(str1 + str2);
          }
     }

     return concatSrings;
}


public ArrayList<String> removeDuplicatePenalties(ArrayList<String> concatenatedPenalty)
{
     String alElement;

     ArrayList<String> nondupAtomTypes = new ArrayList<String>();           

     for (k = 0; k < concatenatedPenalty.size(); k++)
     {
          alElement = concatenatedPenalty.get(k);

          if (!nondupAtomTypes.contains(alElement))
          {
              nondupAtomTypes.add(alElement);
          }
     } 

     return nondupAtomTypes;
}


public ArrayList<String> sortConcatenatedPenalties(ArrayList<String> nondupConcatenatedPenalty, ArrayList<Double> penalties)
{
     int len, maxNdx;
     double maxPenalty;

     ArrayList<Double> tmpPenalty = new ArrayList<Double>();
     ArrayList<String> tmpNondupConcatenatedPenalties = new ArrayList<String>();
     ArrayList<String> sortedConcatenatedPenalties = new ArrayList<String>();

     for (k = 0; k < penalties.size(); k++)
     {
          tmpPenalty.add(penalties.get(k));
          tmpNondupConcatenatedPenalties.add(nondupConcatenatedPenalty.get(k));
     }

     len = tmpPenalty.size();

     while (len > 1)
     {
          maxNdx = 0;

          maxPenalty = tmpPenalty.get(0);

          for (k = 1; k < len; k++)
          {
               if (tmpPenalty.get(k) > maxPenalty)
               {
                    maxPenalty =  tmpPenalty.get(k);
                    maxNdx = k;
               }         
          }

          sortedConcatenatedPenalties.add(tmpNondupConcatenatedPenalties.get(maxNdx));

          tmpPenalty.remove(maxNdx);
          tmpNondupConcatenatedPenalties.remove(maxNdx);

          len = tmpPenalty.size();
     }

     sortedConcatenatedPenalties.add(tmpNondupConcatenatedPenalties.get(0));

     return sortedConcatenatedPenalties;
}

     public ArrayList<String> getAtomTypes(String infilename)
//     public ArrayList<String> getAtomTypes(String infilename)
     {
          int nameNdx, penaltyNdx, dhflag;
          double Q;

          String atomid, input, lineElem1, lineElem2;
          Scanner fileScan = null, lineScan = null;

          File file;
          file = new File(infilename);

          natoms = getNatoms();
          atomNames = getAtomNames();

          atomTypes.clear();
          bondAtomTypes.clear(); angleAtomTypes.clear(); dhAtomTypes.clear(); imprAtomTypes.clear();
          QPenalty.clear();

          dhParamSource.clear();  dhMult.clear();  dhKforce.clear();  dhPhaseAngle.clear();

          rowndxList.clear();  selectedDihedrals.clear();

          for (j = 0; j < natoms; j++)
          {
               atomTypes.add("N/A");
               atomCharges.add(0.0);
          }

          try
          {
               fileScan = new Scanner(file);

               input = (fileScan.nextLine()).trim();
               
               while (fileScan.hasNextLine())
               {
                    input = (fileScan.nextLine()).trim();

                    if (input.startsWith("RESI"))
                    {
                         //
                    }
                    else if (input.startsWith("GROUP"))
                    {
                         for (k = 0; k < natoms; k++)
                         {
                              input = (fileScan.nextLine()).trim();

                              if (input.startsWith("ATOM"))
                              {
                                   lineScan = new Scanner(input);

                                   lineElem1 = lineScan.next();
                                   atomid = lineScan.next();

                                   nameNdx = atomNames.indexOf(atomid);

                                   atomTypes.set(nameNdx, lineScan.next());

                                   Q = Double.parseDouble(lineScan.next());
                                   atomCharges.set(nameNdx, Q); 

                                   if (lineScan.next().equals("!"))
                                   {
                                        QPenalty.add(Double.parseDouble(lineScan.next()));
                                   }

                                   lineScan.close();
                              }
                         }
                    }
                    else if (input.startsWith("BONDS"))
                    {


                    }
                    else if (input.startsWith("ANGLES"))
                    {


                    }
                    else if (input.startsWith("DIHEDRALS"))
                    {
                         int dhSourceNdx1, dhSourceNdx2; 
                         ArrayList<String> dhPenaltyList = new ArrayList<String>();
                         ArrayList<Double> tmpDhPenalties = new ArrayList<Double>();

                         ArrayList<String> tmpDhParamList = new ArrayList<String>();
                         ArrayList<Integer> tmpDhParamIndexList = new ArrayList<Integer>();

                         ArrayList<Integer> sortedDhParamIndexList = new ArrayList<Integer>();
                         ArrayList<Integer> sortedDhPenaltyIndex = new ArrayList<Integer>();

                         ArrayList<String> concatenatedPenalties = new ArrayList<String>();
                         ArrayList<String> nondupConcatenatedPenalties = new ArrayList<String>();
                         ArrayList<String> sortedConcatenatedPenalties = new ArrayList<String>();

                         ArrayList<String> sortedDhAtomTypes = new ArrayList<String>();
                         ArrayList<Double> sortedDhPenalties = new ArrayList<Double>();

                         String strDhAtomtypes = new String();
                         String strDhPenalty = new String();

                         dhflag = 1;
                         dhAtmTypCellWidth = 0;

                         if (fileScan.hasNextLine())
                         {
                              while (dhflag == 1)
                              {
                                   input = (fileScan.nextLine()).trim();
             
                                   if ( (input.equals("")) || (input.equals("IMPROPERS")) )
                                   {
                                        dhflag = 0;
                                   }
                                   else
                                   {
                                        lineScan = new Scanner(input);
                                        
                                        for (k = 0; k < 4; k++)
                                        {  
                                             strDhAtomtypes += "  " + lineScan.next();
                                        }


                                        dhAtomTypes.add(strDhAtomtypes);

                                        dhAtmTypCellWidth = Math.max(dhAtmTypCellWidth, strDhAtomtypes.length()); 
                                        
                                        // Clear contents of strDhAtomtypes string
                                        strDhAtomtypes = new String();
                                        
                                        // Read in force constants, multiplicities, and dihedral angles
                                        dhKforce.add(Double.parseDouble(lineScan.next()));                // dihedral force constant

                                        dhMult.add(Integer.parseInt(lineScan.next()));                    // dihedral multiplicity

                                        dhPhaseAngle.add(Double.parseDouble(lineScan.next()));            // dihedral phase angle

                                        // Get position of dihedral penalty value
                                        penaltyNdx = input.lastIndexOf("=");

                                        strDhPenalty = input.substring(penaltyNdx + 1).trim();

                                        dhPenaltyList.add(strDhPenalty);
                                        dhPenalties.add(Double.parseDouble(strDhPenalty));
                                        tmpDhPenalties.add(Double.parseDouble(strDhPenalty));

                                        // Get position of dihedral parameter data source
                                        dhSourceNdx1 = input.lastIndexOf("m");
                                        dhSourceNdx2 = input.lastIndexOf(",");

                                        dhParamSource.add( (input.substring((dhSourceNdx1 + 1), (dhSourceNdx2 - 1))).trim() );

                                        lineScan.close();
                                   }
                              }
                         }

                         tableRowCount = dhAngleTableModel.getRowCount();

                         for (k = 0; k < tableRowCount; k++)
                         {
                              dhAngleTableModel.removeRow(0);
                         }


//dhAngleTableModel = new DefaultTableModel(tabldata, colnames);


                         // Eliminate duplicate parameters having the same penalty

                              // (1) Concatenate dihedral atom types and dihedral penalties
                                concatenatedPenalties = concatenateWithPenalty(dhAtomTypes, dhPenaltyList);

                              // (2) Remove duplicate dihedral atom atoms that have the same dihedral penalties
                                nondupConcatenatedPenalties = removeDuplicatePenalties(concatenatedPenalties);

                              // (3) Sort concatenated atom types in desxending order of penalties
                                sortedConcatenatedPenalties = sortConcatenatedPenalties(nondupConcatenatedPenalties, tmpDhPenalties);

                                int sortedNdx = 0;

                              // Sort 'dhAtomTypes' and 'dhPenalties' from 'sortedConcatenatedPenalties' 
                                for (k = 0; k < sortedConcatenatedPenalties.size(); k++)
                                {
                                     sortedNdx = nondupConcatenatedPenalties.indexOf(sortedConcatenatedPenalties.get(k));

                                     sortedDhAtomTypes.add(dhAtomTypes.get(sortedNdx));
                                     sortedDhPenalties.add(dhPenalties.get(sortedNdx));
                                }

                         // Add sorted 'dhAtomTypes' and 'dhPenalties' to dhParametersTable  
                           for (k = 0; k < dhAtomTypes.size(); k++)
                           {
                                dhAngleTableModel.insertRow(k, new Object[] {(k+1), sortedDhAtomTypes.get(k), sortedDhPenalties.get(k)} );
                           }                        

                           dhParametersTable.getColumnModel().getColumn(0).setPreferredWidth(25);
                           dhParametersTable.getColumnModel().getColumn(1).setPreferredWidth(275);
                           dhParametersTable.getColumnModel().getColumn(2).setPreferredWidth(100);

                           // Clear unneeded array lists
                           concatenatedPenalties.clear();  nondupConcatenatedPenalties.clear();  sortedConcatenatedPenalties.clear();
                    }

                    else if (input.startsWith("IMPROPERS"))
                    {

                    }
               }                    

               //fileScan.close();
               //file.delete();
          }
          catch (FileNotFoundException ex)
          {

          }finally{
        	  fileScan.close();
        	  lineScan.close();
          }

          // Enable display tables menu items in Display menu
//          displayAtomTypeTableItem.setEnabled(true);
//          displayBondParametersTableItem.setEnabled(true);
          displayDihedralParametersTableItem.setEnabled(true);    

          return atomTypes;
     }
     
     public void readDihedrals() {
    	 
    	 int dhIndx;
    	 String strDHAtomTypes = new String();
    	 
         arrDHAtomTypes = new String[ndihedrals];

         for (j = 0; j < ndihedrals; j++)
         {
              strDHAtomTypes = new String();

              for (k = 0; k < 4; k++)
              {
                   dhIndx = alDihedralAtoms.get((4*j) + k);

                   strDHAtomTypes += atomTypes.get(dhIndx) + " ";
              }

              arrDHAtomTypes[j] = strDHAtomTypes.trim();
         }
    	 
     }
     
     ArrayList<Double> getAtomCharges()
     {
          return atomCharges;
     }


//     public void readCoordsFile(String infilename, int nbondvectors) throws FileNotFoundException 
     public void readCoordsFile(String infilename) throws FileNotFoundException 
     { 
          int dotextNdx, indx, i, j, k, linenbr, atomIdNdx, elementIndx, nspaces, spacing;
          int nnatoms, nbondvecs, atomIndex, bvIndex, ndx1, ndx2, bndOrdr;

          double X, Y, Z;
          double bondOrder, canvasDimen;
          double[] maxCoord = new double[ncoords];
          double[] minCoord = new double[ncoords];
          double[][] coordinates;

          String str, ext, fileData, title, input, lnelem, firstLnElem, strAtomNum, strAtomName, strBondNum, element, triposAtomType, strBndOrdr;
          String[] atomId;
          String[] spaces;

          Character chr;

          File filename = new File(infilename);

          Scanner fileScan, lineScan, lnelemScan;
          
          nbondvecs = 0; 

          atomIndex = bvIndex = 0;
          linenbr = 0; nspaces = 15; 

          dotextNdx = infilename.lastIndexOf(".");
          ext = infilename.substring((dotextNdx + 1));


////////////////////

          atomTypes.clear(); 
          bondAtomTypes.clear(); angleAtomTypes.clear(); dhAtomTypes.clear(); imprAtomTypes.clear();
          QPenalty.clear(); 
          bondPenalty.clear(); bondAnglePenalty.clear(); dhPenalties.clear();

////////////////////


          if (!altKeyDown)
          {
               elemID.clear();

               // Clear all bond vectors
               bondVectors = new double[0][0];

               maxCoord[0] = maxCoord[1] = maxCoord[2] = (-1000000);
               minCoord[0] = minCoord[1] = minCoord[2] = 1000000;

               // Reset scale factors
               molScaleFactor = 1.0;  canvasScaleFactor = 1.0;

               nnatoms = nsubstructures = 0;

               if (ext.equals("mol2"))
               {
                    try
                    {
                         fileScan = new Scanner(filename);

                         input = (fileScan.nextLine()).trim();

                         while (fileScan.hasNextLine())
                         {
                              input = (fileScan.nextLine()).trim();
  
                              if (input.startsWith("#"))
                              {
                                   // ignore comment line
                              
                                   input = fileScan.nextLine();
                              }

                              if (input.equals(""))
                              {
                                   // ignore blank line
                              
                                   input = fileScan.nextLine();
                              }

                              if (input.startsWith("@<TRIPOS>MOLECULE"))
                              {
                                   input = fileScan.nextLine();
                                   input = fileScan.nextLine();

                                   lineScan = new Scanner(input);
                                   nnatoms = Integer.parseInt(lineScan.next());
                                   nbonds = Integer.parseInt(lineScan.next());
                                   nfragments = Integer.parseInt(lineScan.next());

                                   atomCoordinates = new double[nnatoms][ncoordCols];
                                   atomConnectivity = new int[nnatoms][nnatoms];

                                   elementRGB = new float[nnatoms][ncolors]; 

                                   input = fileScan.nextLine().trim();
                                   input = fileScan.nextLine().trim();
                                   input = fileScan.nextLine().trim();

                                   // Read number of bond vectors if this molecule is a functional group
                                   if (input.startsWith("##nbv"))
                                   {
                                        lineScan = new Scanner(input);
                                        lineScan.next();
                                        nbondvecs = Integer.parseInt(lineScan.next());

                                        input = fileScan.nextLine();
                                   }
                              }

                              if (input.startsWith("@<TRIPOS>ATOM"))
                              {
                                   coordinates = new double[nnatoms][ncoords];

                                   for (j = 0; j < nnatoms; j++)
                                   { 
                                        input = fileScan.nextLine();

                                        lineScan = new Scanner(input);
                                        
                                        strAtomNum = lineScan.next();
                                        strAtomName = lineScan.next();

                                        element = new String();

                                        for (k = 0; k < strAtomName.length(); k++)
                                        {
                                             chr = strAtomName.charAt(k);

                                             if (Character.isLetter(chr))
                                             {
                                                  element = element + chr;
                                             }
                                        }

                                        elemID.add(element);

                                        atomCoordinates[j][0] = j;

                                        for (k = 0; k < ncoords; k++)
                                        {
                                             coordinates[j][k] = Double.parseDouble(lineScan.next());

                                             atomCoordinates[j][(k+1)] = coordinates[j][k];
                                       
                                             maxCoord[k] = Math.max(atomCoordinates[j][(k+1)], maxCoord[k]);
                                             minCoord[k] = Math.min(atomCoordinates[j][(k+1)], minCoord[k]);
                                        }

                                        triposAtomType = lineScan.next();

                                        atomCoordinates[j][4] = Integer.parseInt(lineScan.next());
                                   }

                                   if ( (!undo) && (!redo) )
                                   {
                                        setDelCoords(nnatoms, coordinates);
                                   }
 
                                   if (nbondvecs > 0)
                                   {
                                        bondVectors = new double[nbondvecs][nbvCols];

                                        for (j = 0; j < nbondvecs; j++)       
                                        {
                                             input = (fileScan.nextLine()).trim();

                                             lineScan = new Scanner(input);
                                        
                                             str = lineScan.next();

                                             if (str.equals("##"))
                                             {
                                                  str = lineScan.next();

                                                  if (str.equals("bv"))
                                                  {
                                                       bondVectors[j][1] = Double.parseDouble(lineScan.next());
                                                       bondVectors[j][2] = Double.parseDouble(lineScan.next());
                                                       bondVectors[j][3] = Double.parseDouble(lineScan.next());
                                                       bondVectors[j][0] = Integer.parseInt(lineScan.next());
                                                       bondVectors[j][4] = Integer.parseInt(lineScan.next());
                                                       bondVectors[j][5] = 1;
                                                  }
                                             }
                                        }
                                   }
                              }
                              else if (input.startsWith("@<TRIPOS>BOND"))
                              {
                                   for (j = 0; j < nbonds; j++)
                                   { 
                                        input = fileScan.nextLine();

                                        lineScan = new Scanner(input);
                                        
                                        strBondNum = lineScan.next();
                                        ndx1 = Integer.parseInt(lineScan.next()) - 1;
                                        ndx2 = Integer.parseInt(lineScan.next()) - 1;

                                        strBndOrdr = lineScan.next();

                                        if (strBndOrdr.equals("ar"))
                                        {
                                             bndOrdr = 4;
                                        }
                                        else
                                        {
                                              // bndOrdr = Integer.parseInt(lineScan.next());

                                             bndOrdr = Integer.parseInt(strBndOrdr);
                                        }
/////////////

                              if (bndOrdr == 4)
                              {
                                   bndOrdr = 2;
                              }
/////////////

                                        atomConnectivity[ndx1][ndx2] = atomConnectivity[ndx2][ndx1] = bndOrdr;
                                   }
                              }

/*

                              else if (input.startsWith("@<TRIPOS>BOND"))
                              {
                                   for (j = 0; j < nbonds; j++)
                                   { 
                                        input = fileScan.nextLine();

                                        lineScan = new Scanner(input);
                                        
                                        strBondNum = lineScan.next();
                                        ndx1 = Integer.parseInt(lineScan.next()) - 1;
                                        ndx2 = Integer.parseInt(lineScan.next()) - 1;
                                        bndOrdr = Integer.parseInt(lineScan.next());

                                        atomConnectivity[ndx1][ndx2] = atomConnectivity[ndx2][ndx1] = bndOrdr;
                                   }
                              }
*/
                         }

                         nbondvectors = nbondvecs;

                         nsubstructures++;

                         atomNames.clear();
                         setAtomNames(elemID);

                         atomNames = getAtomNames();

                         AN = getAN(elemID, nnatoms);
                         atomRadii = getAtomRadii(AN);

                         elementRGB = new float[nnatoms][ncolors];
                         setElementRGB(AN, nnatoms); 

                         elementRGB = getElementColors();         

                         natoms = nnatoms;

                         // If adding functional group or molecular fragment to blank canvas, set
                         //   number of fragment atoms equal to number of atoms read in from file
                         if ( (addFcnalGrp) || (addFragment) )
                         {
                              nfragatoms = natoms;
                         }

                         fileScan.close();

                         maxMolDimen = maxCoord[0] - minCoord[0];

                         for (j = 1; j < ncoords; j++)
                         { 
                              maxMolDimen = Math.max( (maxCoord[j] - minCoord[j]), maxMolDimen);
                         }

                         canvasDimen = ang2pixls*minCanvasDimen;

                         if (maxMolDimen >= canvasDimen)
                         {
                              canvasScaleFactor = 0.90*(canvasDimen/maxMolDimen);

                         }
                         else
                         {
                              canvasScaleFactor = 1.0;
                         }
                    }
                    catch (FileNotFoundException ex)
                    {
                         System.out.println("Error:  " + filename + " not found");
                    }            
               }

               setElementRGB(AN, natoms); 

               elementRGB = new float[natoms][ncolors];
               elementRGB = getElementColors();

               selectedNames.clear();

               // Identify all unique dihedral atoms in molecule when molecule is first displayed

               alDihedralAtoms.clear();

               identifyDihedralAtoms(natoms, atomConnectivity);

               ndihedrals = getNdihedrals();
               arrDihedralAtoms = new int[ndihedrals][4];

               arrDihedralAtoms = getDihedralAtoms();
          }
     }

     public void identifyDihedralAtoms(int natoms, int[][] atomConnectivity)
     {
          // Get atoms forming every dihedral angle in molecule
          int dihedralNdx, dhNdx1, dhNdx2, dhNdx3, dhNdx4;

          ArrayList<String> strDHAtomIndices = new ArrayList<String>();
          ArrayList<String> strDHAtomTypes = new ArrayList<String>();

          String indexList, indexList1, indexList2;

          for (dhNdx1 = 0; dhNdx1 < natoms; dhNdx1++)
          {
               for (dhNdx2 = 0; dhNdx2 < natoms; dhNdx2++)
               { 
                    if (atomConnectivity[dhNdx1][dhNdx2] > 0)    // if atoms 1 and 2 are bonded
                    {
                         for (dhNdx3 = 0; dhNdx3 < natoms; dhNdx3++)
                         {
                              if ( (atomConnectivity[dhNdx2][dhNdx3] > 0) && (dhNdx3 != dhNdx1) )   // if atoms 2 and 3 are bonded and atom 3 is not atom 1 
                              {     
                                   for (dhNdx4 = 0; dhNdx4 < natoms; dhNdx4++)
                                   {    
                                        if (atomConnectivity[dhNdx3][dhNdx4] > 0)    // if atoms 3 and 4 are bonded
                                        {
                                             if( (dhNdx4 != dhNdx2) && (dhNdx4 != dhNdx1) && (atomConnectivity[dhNdx4][dhNdx1] == 0) && (atomConnectivity[dhNdx4][dhNdx2] == 0) )  // if atom 4 is not atom (2 or 1) and atoms (4 and 1) and (4 and 2) are not bonded 
                                             {
                                                  indexList1 = Integer.toString(dhNdx1) + "-" + Integer.toString(dhNdx2) + "-" + Integer.toString(dhNdx3) + "-" + Integer.toString(dhNdx4);
                                                  indexList2 = Integer.toString(dhNdx4) + "-" + Integer.toString(dhNdx3) + "-" + Integer.toString(dhNdx2) + "-" + Integer.toString(dhNdx1);

                                                  if (!strDHAtomIndices.contains(indexList1)) ;
                                                  {
                                                       if (indexList2 != indexList1) ;
                                                       {
                                                            strDHAtomIndices.add(indexList1);

                                                            alDihedralAtoms.add(dhNdx1);  alDihedralAtoms.add(dhNdx2);  alDihedralAtoms.add(dhNdx3);  alDihedralAtoms.add(dhNdx4); 
                                                       }
                                                  }
                                             }
                                        }
                                   }
                              }
                         }
                    }
               }
          }


          ndihedrals = alDihedralAtoms.size()/4;     // 4 atoms for each individual dihedral

          arrDihedralAtoms = new int[ndihedrals][4];

          for (dihedralNdx = 0; dihedralNdx < ndihedrals; dihedralNdx++)
          {
               k = 4*dihedralNdx;

               for (j = 0; j < 4; j++)
               {
                    arrDihedralAtoms[dihedralNdx][j] = alDihedralAtoms.get(k+j);
               }
          }
     }  



public int[][] getDihedralAtoms()
{
     return arrDihedralAtoms;
}


public int getNdihedrals()
{
     return ndihedrals;
}


///////////////////////////////////////









//     public void readFragmentFile(String infilename, int nbondvectors, String fragType) throws FileNotFoundException 
     public void readFragmentFile(String infilename, String fragType) throws FileNotFoundException 
     {
          natoms = getNatoms();

          if (fragType.equals("fragment"))
          {
               addFragment = true;  addFcnalGrp = false;
          }
          else if (fragType.equals("fcnalGrp"))
          {
               addFragment = false;  addFcnalGrp = true;
          }

          if (natoms == 0)
          {
//               readCoordsFile(infilename, nbondvectors);
               readCoordsFile(infilename);
          }
          else
          {
               int dotextNdx, indx, i, j, k, linenbr, atomIdNdx, elementIndx, nspaces, spacing;
               int nfragbvs, nbndvecs, atomIndex, bvIndex, ndx1, ndx2, bndOrdr;

               double X, Y, Z, bondOrder;
               double[][] coordinates;

               String str, ext, fileData, title, input, lnelem, firstLnElem, strAtomNum, strAtomName, strBondNum, element, triposAtomType, strBndOrdr;
               String[] atomId;
               String[] spaces;

               Character chr;

               File filename = new File(infilename);

               Scanner fileScan, lineScan, lnelemScan;
          
               nfragatoms = nfragbonds = nfragbvs = 0;
               
               atomIndex = bvIndex = 0;
               linenbr = 0; nspaces = 15; 

               fragElemID.clear();

               dotextNdx = infilename.lastIndexOf(".");
               ext = infilename.substring((dotextNdx + 1));

               if (ext.equals("mol2"))
               {
                    try
                    {
                         fileScan = new Scanner(filename);

                         input = (fileScan.nextLine()).trim();

                         while (fileScan.hasNextLine())
                         {
                              input = (fileScan.nextLine()).trim();

                              if (input.startsWith("#"))
                              {
                                   // ignore comment line
                              
                                   input = fileScan.nextLine();
                              }

                              if (input.equals(""))
                              {
                                   // ignore blank line
                              
                                   input = fileScan.nextLine();
                              }

                              if (input.startsWith("@<TRIPOS>MOLECULE"))
                              {
                                   input = fileScan.nextLine();
                                   input = fileScan.nextLine();

                                   lineScan = new Scanner(input);

                                   nfragatoms = Integer.parseInt(lineScan.next());
                                   nfragbonds = Integer.parseInt(lineScan.next());
                                   nfragments = Integer.parseInt(lineScan.next());

                                   fragatomCoordinates = new double[nfragatoms][ncoordCols];
                                   fragatomConnectivity = new int[nfragatoms][nfragatoms];

                                   input = fileScan.nextLine().trim();
                                   input = fileScan.nextLine().trim();
                                   input = fileScan.nextLine().trim();

                                   // Read number of bond vectors if this molecule is a functional group
                                   if (input.startsWith("##nbv"))
                                   {
                                        lineScan = new Scanner(input);
                                        lineScan.next();
                                        nfragbvs = Integer.parseInt(lineScan.next());

                                        input = fileScan.nextLine();
                                   }
                                   else if (input.startsWith("##solvndx"))
                                   {
                                        lineScan = new Scanner(input);
                                        lineScan.next();
                                       
                                        solvatedAtomIndices[0] = Integer.parseInt(lineScan.next());
                                        solvatedAtomIndices[1] = Integer.parseInt(lineScan.next());
                                   }
                              }

                              if (input.startsWith("@<TRIPOS>ATOM"))
                              {
                                   for (j = 0; j < nfragatoms; j++)
                                   { 
                                        input = fileScan.nextLine();

                                        lineScan = new Scanner(input);
                                        
                                        strAtomNum = lineScan.next();
                                        strAtomName = lineScan.next();

                                        element = new String();

                                        for (k = 0; k < strAtomName.length(); k++)
                                        {
                                             chr = strAtomName.charAt(k);

                                             if (Character.isLetter(chr))
                                             {
                                                  element = element + chr;
                                             }
                                        }

                                        fragElemID.add(element);

                                        fragatomCoordinates[j][0] = j;
                                     
                                        for (k = 0; k < ncoords; k++)
                                        {
                                             fragatomCoordinates[j][(k+1)] = Double.parseDouble(lineScan.next());
                                        }
                                   }

                                   if (nfragbvs > 0)
                                   {
                                        fragbondVectors = new double[nfragbvs][nbvCols];

                                        for (j = 0; j < nfragbvs; j++)
                                        {
                                             input = fileScan.nextLine();          
 
                                             lineScan = new Scanner(input);

                                             str = lineScan.next();

                                             if (str.equals("##"))
                                             {
                                                  str = lineScan.next();

                                                  if (str.equals("bv"))
                                                  {
                                                       fragbondVectors[j][1] = Double.parseDouble(lineScan.next());
                                                       fragbondVectors[j][2] = Double.parseDouble(lineScan.next());
                                                       fragbondVectors[j][3] = Double.parseDouble(lineScan.next());
                                                       fragbondVectors[j][0] = Integer.parseInt(lineScan.next());
                                                       fragbondVectors[j][4] = Integer.parseInt(lineScan.next());
                                                       fragbondVectors[j][5] = 1;
                                                  }
                                             }
                                        }     
                                   }
                              }
                              else if (input.startsWith("@<TRIPOS>BOND"))
                              {
                                   for (j = 0; j < nfragbonds; j++)
                                   { 
                                        input = fileScan.nextLine();

                                        lineScan = new Scanner(input);
                                        
                                        strBondNum = lineScan.next();
                                        ndx1 = Integer.parseInt(lineScan.next()) - 1;
                                        ndx2 = Integer.parseInt(lineScan.next()) - 1;

                                        strBndOrdr = lineScan.next();

                                        if (strBndOrdr.equals("ar"))
                                        {
                                             bndOrdr = 4;
                                        }
                                        else
                                        {
                                              // bndOrdr = Integer.parseInt(lineScan.next());

                                             bndOrdr = Integer.parseInt(strBndOrdr);
                                        }
/////////////

                              if (bndOrdr == 4)
                              {
                                   bndOrdr = 2;
                              }
/////////////

                                        fragatomConnectivity[ndx1][ndx2] = fragatomConnectivity[ndx2][ndx1] = bndOrdr;
                                   }
                              }
                         }


/*
                              else if (input.startsWith("@<TRIPOS>BOND"))
                              {
                                   for (j = 0; j < nfragbonds; j++)
                                   { 
                                        input = fileScan.nextLine();

                                        lineScan = new Scanner(input);
                                        
                                        strBondNum = lineScan.next();
                                        ndx1 = Integer.parseInt(lineScan.next()) - 1;
                                        ndx2 = Integer.parseInt(lineScan.next()) - 1;
                                        bndOrdr = Integer.parseInt(lineScan.next());

                                        fragatomConnectivity[ndx1][ndx2] = fragatomConnectivity[ndx2][ndx1] = bndOrdr;
                                   }
                              }
                         }

*/

                         fileScan.close();
                    }
                    catch (FileNotFoundException ex)
                    {
                         System.out.println("Error:  " + filename + " not found");
                    }            
               }

               nbondvectors = nfragbvs;

               selectedNames.clear();
          }     
     }

     public void writeNewMol2File(String strMol2Filename)
     {
          int nbondvecs, userDefFileNumbr, initndx, initsuffix, finalsuffix, nbvsarrsize, delsuffix;
          int maxnspcs;

          String mol2Text = new String();
          String nbvfilename = new String();
          String space, spc, spc0, spc1, spc2, spc3, spc4, spc5, spc6;
          String str, prefix;
          String atmname, infilename, molName;

          File mol2Filename;

          Formatter fmt;  

          space = new String();

          BufferedWriter mol2outfile;

          mol2Filename = new File(strMol2Filename);
          molName = moleculeName;

          spc0 = null; spc1 = " ";  spc2 = "  ";  spc3 = "   ";  spc4 = "    ";  spc5 = "     ";  spc6 = "      ";

          maxnspcs = 7;

          try
          {
               mol2outfile = new BufferedWriter(new FileWriter(mol2Filename));

               mol2outfile.write("# Name: " + molName + "\n");
               mol2outfile.write("\n");
               mol2outfile.write("@<TRIPOS>MOLECULE" + "\n");
               mol2outfile.write(molName + "\n");
               mol2outfile.write(natoms + spc1 + nbonds + spc1 + nsubstructures + spc1 + 0 + spc1 + 0 + "\n");
               mol2outfile.write("SMALL" + "\n");
               mol2outfile.write("NO_CHARGE" + "\n");

               if (nbondvectors > 0)
               {
                    mol2outfile.write("##nbv " + nbondvectors + "\n");  
               }
               else if (atomSolvated)
               {
                    mol2outfile.write("##solvndx  " + solvatedAtomIndices[0] + spc2 + solvatedAtomIndices[1] + "\n");
               }

               mol2outfile.write("\n");

               mol2outfile.write("@<TRIPOS>ATOM" + "\n");
              
               fmt = new Formatter(mol2outfile);
 
               for (ndx = 0; ndx < natoms; ndx++)
               {
                    // Get length of atom name
                    atmname = atomNames.get(ndx);

                    fmt.format("%5d %5s %s % .4f", (ndx+1), atomNames.get(ndx), spc3, (double)(Math.round(atomCoordinates[ndx][1]*10000))/10000);
                    fmt.format("%s % .4f %s % .4f", spc3, (double)(Math.round(atomCoordinates[ndx][2]*10000))/10000, spc3, (double)(Math.round(atomCoordinates[ndx][3]*10000))/10000);
                    fmt.format("%s %2s %s %2d\n", spc2, elemID.get(ndx), spc2, (int)atomCoordinates[0][4]); 
               }

               // Write bond vectors to mol2 file
               nbondvecs = bondVectors.length;

               if (nbondvecs > 0)
               {
                    prefix = new String("##   bv    ");

                    for (ndx = 0; ndx < nbondvecs; ndx++)
                    {
                         fmt.format("%s %s % .4f", prefix, spc3, (double)(Math.round(bondVectors[ndx][1]*10000))/10000); 
                         fmt.format("%s % .4f %s % .4f", spc3, (double)(Math.round(bondVectors[ndx][2]*10000))/10000, spc3, (double)(Math.round(bondVectors[ndx][3]*10000))/10000); 
                         fmt.format("%s %d %s %d\n", spc2, (int)bondVectors[ndx][0], spc1, (int)bondVectors[ndx][4]);
                    }
               }

               if (natoms > 1)
               {
                    mol2outfile.write("@<TRIPOS>BOND" + "\n");

                    indx = 0;

                    for (j = 0; j < natoms; j++)
                    {
                         for (k = (j+1); k < natoms; k++)
                         {
                              if (atomConnectivity[j][k] > 0)
                              {
                                   fmt.format("%2d %s %2d %s %2d %s", indx, spc3, (j+1), spc2, (k+1), spc2);  
                             
                                   if (atomConnectivity[j][k] == 4)
                                   {
                                        fmt.format("%s\n", "ar");
                                   }
                                   else
                                   {
                                        fmt.format("%2d\n", (int)atomConnectivity[j][k]);
                                   }
                              }
                         }
                    }
               }

               mol2outfile.write("@<TRIPOS>SUBSTRUCTURE" + "\n");
               mol2outfile.write(" " + 1 + "   " + molName + 1 + "   " + 1 + "\n");

               mol2outfile.close();
          }
          catch (IOException e)
          {
               e.printStackTrace();
          }    
     }


public void printSomething()
{
     System.out.println("I am printing something now!!");

     keyID = "0";
}


/////////////

public double[][] rotateStructure(double[][] atomCoordinates, double[] globalRotationAxis, double mux, double muy, double muz, int natoms)
//public void rotateStructure(double[][] atomCoordinates, double[] globalRotationAxis, double mux, double muy, double muz, int natoms)
{
     double n1, n2, n3, mu;
     double crdX, xrdY, crdZ;

     double[][] tmpStructureCoords = new double[natoms][ncoords];

     n1 = globalRotationAxis[0];  n2 = globalRotationAxis[1];  n3 = globalRotationAxis[2];

     mu = (180/Math.PI)*mux;

     for (j = 0; j < natoms; j++)
     {
          if (n1 == 1)
          {
               tmpStructureCoords[j][0] = atomCoordinates[j][1];
               tmpStructureCoords[j][1] = Math.cos(mu)*atomCoordinates[j][2] - Math.sin(mu)*atomCoordinates[j][3];
               tmpStructureCoords[j][2] = Math.sin(mu)*atomCoordinates[j][2] + Math.cos(mu)*atomCoordinates[j][3];

//               tmpStructureCoords[j][0] = atomCoordinates[j][1];
//               tmpStructureCoords[j][1] = Math.cos(mu)*atomCoordinates[j][2] + Math.sin(mu)*atomCoordinates[j][3];
//               tmpStructureCoords[j][2] = -Math.sin(mu)*atomCoordinates[j][2] + Math.cos(mu)*atomCoordinates[j][3];

          }
          else if (n2 == 1)
          {
//               tmpStructureCoords[j][0] = Math.cos(muy)*atomCoordinates[j][1] + Math.sin(muy)*atomCoordinates[j][3];
//               tmpStructureCoords[j][1] = atomCoordinates[j][2];
//               tmpStructureCoords[j][2] = -Math.sin(muy)*atomCoordinates[j][1] + Math.cos(muy)*atomCoordinates[j][3];

               tmpStructureCoords[j][0] = Math.cos(muy)*atomCoordinates[j][1] - Math.sin(muy)*atomCoordinates[j][3];
               tmpStructureCoords[j][1] = atomCoordinates[j][2];
               tmpStructureCoords[j][2] = Math.sin(muy)*atomCoordinates[j][1] + Math.cos(muy)*atomCoordinates[j][3];
          }
          else
          {
//               tmpStructureCoords[j][0] = Math.cos(muz)*atomCoordinates[j][1] - Math.sin(muz)*atomCoordinates[j][2];
//               tmpStructureCoords[j][2] = Math.sin(muz)*atomCoordinates[j][1] + Math.cos(muz)*atomCoordinates[j][2];
//               tmpStructureCoords[j][1] = atomCoordinates[j][3];

               tmpStructureCoords[j][0] = Math.cos(muz)*atomCoordinates[j][1] + Math.sin(muz)*atomCoordinates[j][2];
               tmpStructureCoords[j][2] = -Math.sin(muz)*atomCoordinates[j][1] + Math.cos(muz)*atomCoordinates[j][2];
               tmpStructureCoords[j][1] = atomCoordinates[j][3];

          }
      }

     for (j = 0; j < natoms; j++)
     {
          for (k = 0; k < ncoords; k++)
          {
               atomCoordinates[j][(k+1)] = tmpStructureCoords[j][k];
          }
     }

     return atomCoordinates;
}

/////////////


     public void generateMol2File(String molName)
     { 
          int nbondvecs, userDefFileNumbr, initndx, initsuffix, finalsuffix, nbvsarrsize, delsuffix;

          int strlen, nwspcs, maxnspcs;
          String mol2Text = new String();
          String nbvfilename = new String();
          String space, spc, spc0, spc1, spc2, spc3, spc4, spc5, spc6;
          String str, prefix;
          String atmname, infilename;
          File fname, srcfile, destfile;
          Formatter fmt;

          mol2filename = new String();
          tmpMol2filename = new String();

          space = new String();

          BufferedWriter mol2outfile, nbvoutfile;

          // Get user defined file suffix
          userDefFileSuffix = getUserDefFileSuffix();

          userDefFileNumbr = userDefFileSuffix + 1000;

          mol2filename = moleculeName;     
          tmpMol2filename = mol2filename + Integer.toString(userDefFileNumbr);

          tmpUserDefStructureDir = new File(strTmpUserDefStructureDir);

          // Create directory if it does not already exist
          if (!tmpUserDefStructureDir.exists())
          {
               tmpUserDefStructureDir.mkdir();
          }

          nbondvectors = bondVectors.length;

          nbvsarrsize = userDefnbvs.size();

          if ( userDefFileSuffix < (nbvsarrsize + 1) )
          {
               initsuffix = userDefFileSuffix;
               finalsuffix = nbvsarrsize;

               delsuffix = finalsuffix - initsuffix;

               while (delsuffix > 0)
               {
                    finalsuffix = initsuffix + delsuffix;

                    userDefFileNumbr = finalsuffix + 1000;

                    mol2filename = moleculeName; 
                    tmpMol2filename = mol2filename + Integer.toString(userDefFileNumbr);

                    infilename = userDefStructureDir + mol2filename + ".mol2";
                    fname = new File(infilename);                   

                    fname.delete();

                    // Remove userDefnbvs indices corresponding to deleted files
                    userDefnbvs.remove((finalsuffix - 1));

                    // Remove user defined rotation angles corresponding to deleted files
                    usrdefTheta.remove((finalsuffix - 1));
                    usrdefPhi.remove((finalsuffix - 1));
                    usrdefPsi.remove((finalsuffix - 1));

                    delsuffix--;
               }

               // Write new user defined structure to file
               userDefFileSuffix = initsuffix + 1;
               userDefFileNumbr = userDefFileSuffix + 1000;

               mol2filename = moleculeName; 
               tmpMol2filename = mol2filename + Integer.toString(userDefFileNumbr);

               currentMol2Filename = tmpMol2filename;
          }

          userDefnbvs.add(nbondvectors);

          usrdefTheta.add(getTheta());  usrdefPhi.add(getPhi());  usrdefPsi.add(getPsi());

          natoms = getNatoms();
          nbonds = getNbonds();
          nsubstructures = getNsubstructures();

          atomCoordinates = getCoordinates();
          atomConnectivity = getAtomConnectivity(); 
          atomNames = getAtomNames();


/*
          try
          {
               mol2outfile = new BufferedWriter(new FileWriter(userDefStructureDir + mol2filename + ".mol2"));

               mol2outfile.write("# Name: " + molName + "\n");
               mol2outfile.write("\n");
               mol2outfile.write("@<TRIPOS>MOLECULE" + "\n");
               mol2outfile.write(molName + "\n");
               mol2outfile.write(natoms + " " + nbonds + " " + nsubstructures + " " + 0 + " " + 0 + "\n");
               mol2outfile.write("SMALL" + "\n");
               mol2outfile.write("NO_CHARGE" + "\n");

               if (nbondvectors > 0)
               {
                    mol2outfile.write("##nbv " + nbondvectors + "\n");  
               }
               else if (atomSolvated)
               {
                    mol2outfile.write("##solvndx  " + solvatedAtomIndices[0] + "  " + solvatedAtomIndices[1] + "\n");
               }

               mol2outfile.write("\n");

               mol2outfile.write("@<TRIPOS>ATOM" + "\n");
               
               for (ndx = 0; ndx < natoms; ndx++)
               {
                    spc = "";  spc1 = "    "; spc2 = "     ";

                    if (ndx < 9)
                    {
                         spc = " ";
                    }

                    if ((atomNames.get(ndx)).length() > 2)
                    {
                         spc2 = "    ";
                    }

                    mol2outfile.write(spc + (ndx+1) + spc1 + atomNames.get(ndx) + spc2 + atomCoordinates[ndx][1] + "    ");
                    mol2outfile.write(atomCoordinates[ndx][2] + "    " + atomCoordinates[ndx][3] + "    ");
                    mol2outfile.write(elemID.get(ndx) + "    " + (int)atomCoordinates[0][4] + "\n");
               }

               // Write bond vectors to mol2 file
               nbondvecs = bondVectors.length;

               if (nbondvecs > 0)
               {
                    for (ndx = 0; ndx < nbondvecs; ndx++)
                    {
                         mol2outfile.write("##    bv  " + bondVectors[ndx][1] + "    " +  bondVectors[ndx][2] + "    ");
                         mol2outfile.write(bondVectors[ndx][3] + "    " + (int)bondVectors[ndx][0] + "    " + (int)bondVectors[ndx][4] + "\n");
                    }
               }

               if (natoms > 1)
               {
                    mol2outfile.write("@<TRIPOS>BOND" + "\n");

                    indx = 0;

                    for (j = 0; j < natoms; j++)
                    {
                         for (k = (j+1); k < natoms; k++)
                         {
                              if (atomConnectivity[j][k] > 0)
                              {
                                   indx++;  spc = "";  spc1 = "    ";

                                   if (indx < 10)
                                   {
                                        spc = " ";
                                   }

                                   mol2outfile.write(spc + indx + spc1 + (j+1) + "   " + (k+1) + "   " + atomConnectivity[j][k] + "\n");
                              }
                         }
                    }
               }

               mol2outfile.write("@<TRIPOS>SUBSTRUCTURE" + "\n");
               mol2outfile.write(" " + 1 + "   " + molName + 1 + "   " + 1 + "\n");

               mol2outfile.close();

               // Copy file from User Defined Structures to tmpUserDefStructure with user defined file number suffix appended
               srcfile = new File(userDefStructureDir + mol2filename + ".mol2");
               destfile = new File(strTmpUserDefStructureDir + tmpMol2filename + ".mol2");

               copyFile2File(srcfile, destfile);
          }
*/

          spc0 = null; spc1 = " ";  spc2 = "  ";  spc3 = "   ";  spc4 = "    ";  spc5 = "     ";  spc6 = "      ";

          maxnspcs = 7;

          try
          {
               mol2outfile = new BufferedWriter(new FileWriter(userDefStructureDir + mol2filename + ".mol2"));

               mol2outfile.write("# Name: " + molName + "\n");
               mol2outfile.write("\n");
               mol2outfile.write("@<TRIPOS>MOLECULE" + "\n");
               mol2outfile.write(molName + "\n");
               mol2outfile.write(natoms + spc1 + nbonds + spc1 + nsubstructures + spc1 + 0 + spc1 + 0 + "\n");
               mol2outfile.write("SMALL" + "\n");
               mol2outfile.write("NO_CHARGE" + "\n");

               if (nbondvectors > 0)
               {
                    mol2outfile.write("##nbv " + nbondvectors + "\n");  
               }
               else if (atomSolvated)
               {
                    mol2outfile.write("##solvndx  " + solvatedAtomIndices[0] + spc2 + solvatedAtomIndices[1] + "\n");
               }

               mol2outfile.write("\n");

               mol2outfile.write("@<TRIPOS>ATOM" + "\n");
              
               fmt = new Formatter(mol2outfile);
 
               for (ndx = 0; ndx < natoms; ndx++)
               {
                    // Get length of atom name
                    atmname = atomNames.get(ndx);

//                    strlen = atmname.length();

//                    nwspcs = (maxnspcs - strlen);

                    fmt.format("%5d %5s %s % .4f", (ndx+1), atomNames.get(ndx), spc3, (double)(Math.round(atomCoordinates[ndx][1]*10000))/10000);
                    fmt.format("%s % .4f %s % .4f", spc3, (double)(Math.round(atomCoordinates[ndx][2]*10000))/10000, spc3, (double)(Math.round(atomCoordinates[ndx][3]*10000))/10000);
                    fmt.format("%s %2s %s %2d\n", spc2, elemID.get(ndx), spc2, (int)atomCoordinates[0][4]); 
               }

               // Write bond vectors to mol2 file
               nbondvecs = bondVectors.length;

               if (nbondvecs > 0)
               {
                    prefix = new String("##   bv    ");

                    for (ndx = 0; ndx < nbondvecs; ndx++)
                    {
                         fmt.format("%s %s % .4f", prefix, spc3, (double)(Math.round(bondVectors[ndx][1]*10000))/10000); 
                         fmt.format("%s % .4f %s % .4f", spc3, (double)(Math.round(bondVectors[ndx][2]*10000))/10000, spc3, (double)(Math.round(bondVectors[ndx][3]*10000))/10000); 
                         fmt.format("%s %d %s %d\n", spc2, (int)bondVectors[ndx][0], spc1, (int)bondVectors[ndx][4]);
                    }
               }

               if (natoms > 1)
               {
                    mol2outfile.write("@<TRIPOS>BOND" + "\n");

                    indx = 0;

                    for (j = 0; j < natoms; j++)
                    {
                         for (k = (j+1); k < natoms; k++)
                         {
                              if (atomConnectivity[j][k] > 0)
                              {
                                   fmt.format("%2d %s %2d %s %2d %s", indx, spc3, (j+1), spc2, (k+1), spc2);  
                             
                                   if (atomConnectivity[j][k] == 4)
                                   {
                                        fmt.format("%s\n", "ar");
                                   }
                                   else
                                   {
                                        fmt.format("%2d\n", (int)atomConnectivity[j][k]);
                                   }
                              }
                         }
                    }
               }

               mol2outfile.write("@<TRIPOS>SUBSTRUCTURE" + "\n");
               mol2outfile.write(" " + 1 + "   " + molName + 1 + "   " + 1 + "\n");

               mol2outfile.close();

               // Copy file from User Defined Structures to tmpUserDefStructure with user defined file number suffix appended
               srcfile = new File(userDefStructureDir + mol2filename + ".mol2");
               destfile = new File(strTmpUserDefStructureDir + tmpMol2filename + ".mol2");

               copyFile2File(srcfile, destfile);
          }
          catch (IOException e)
          {
               e.printStackTrace();
          }    
     }           


     public void copyFile2File(File srcFile, File destFile)
     {
          int mrk;

          FileReader infile;
          FileWriter outfile;

          try
          {
               infile = new FileReader(srcFile);
               outfile = new FileWriter(destFile);

               while ((mrk = infile.read()) != -1)
               {
                    outfile.write(mrk);
               }

               infile.close();
               outfile.close();
          }
          catch (IOException ioe)
          {
               ioe.printStackTrace();
          }    
     }

     public void writeSolvatedAtomFiles(String molName, int targetAtomNdx, double[][] solventCoordinates)
     {
          int nbondvecs, userDefFileNumbr, initndx, initsuffix, finalsuffix, nbvsarrsize, delsuffix;
          int noxygens, nhydrogens;
          String mol2Text = new String();
          String spc, spc1, spc2;
          String infilename, elemid, solvinputfile;
          File fname;

          mol2filename = new String();

          BufferedWriter mol2outfile;

          natoms = getNatoms();
          nbonds = getNbonds();
          nsubstructures = getNsubstructures();

          atomCoordinates = getCoordinates();
          atomConnectivity = getAtomConnectivity(); 
          atomNames = getAtomNames();

          // Display unsolvated molecule then solvate a new unsolvated atom
          try
          {
               theta = getTheta();  phi = getPhi();  psi = getPsi();

               readCoordsFile(displayedFile);
          }
          catch (IOException e)
          {
               e.printStackTrace();
          }    

//          solvatedMoleculesDir = new File(paramchemDir + strSolvatedMoleculesDir);
          solvatedMoleculesDir = new File(strSolvatedMoleculesDir);

          // Create directory if it does not already exist
          if (!solvatedMoleculesDir.exists())
          {
               solvatedMoleculesDir.mkdir();
          }

//          solvatedAtomsDir = new File(solvatedMoleculesDir + "\\" + moleculeName);
          solvatedAtomsDir = new File(solvatedMoleculesDir + fsep + moleculeName);

          // Create directory for solvated atoms in displayed molecule if it does not already exist
          if (!solvatedAtomsDir.exists())
          {
               solvatedAtomsDir.mkdir();
          }
        
//          solvatedGaussianFilesDir = new File(solvatedMoleculesDir + "\\" + "Gaussian solvation files");
          solvatedGaussianFilesDir = new File(solvatedMoleculesDir + fsep + "Gaussian solvation files"); 

          // Create directory for Gaussian files for solvated atom in displayed molecule if it does not already exist
          if (!solvatedGaussianFilesDir.exists())
          {
               solvatedGaussianFilesDir.mkdir();
          }

//          gaussianDir1 = new File(solvatedGaussianFilesDir + "\\" + moleculeName);
          gaussianDir1 = new File(solvatedGaussianFilesDir + fsep + moleculeName);


          // Create gaussian directory 1 if it does not already exist
          if (!gaussianDir1.exists())
          {
               gaussianDir1.mkdir();
          }

//          gaussianDir2 = new File(gaussianDir1 + "\\" + atomNames.get(targetAtomNdx));
          gaussianDir2 = new File(gaussianDir1 + fsep + atomNames.get(targetAtomNdx));

          // Create gaussian directory 2 if it does not already exist
          if (!gaussianDir2.exists())
          {
               gaussianDir2.mkdir();
          }

          // Add 3 solvent atoms (O, H, H), 2 solvent bonds (O-H, O-H), and 1 substructure (H2O)
        
          // Write mol2 file for solvated atom
          try
          {
//               mol2outfile = new BufferedWriter(new FileWriter(solvatedAtomsDir + "\\" + molName + ".mol2"));
               mol2outfile = new BufferedWriter(new FileWriter(solvatedAtomsDir + fsep + molName + ".mol2"));

               mol2outfile.write("# Name: " + molName + "\n");
               mol2outfile.write("\n");
               mol2outfile.write("@<TRIPOS>MOLECULE" + "\n");
               mol2outfile.write(molName + "\n");
               mol2outfile.write((natoms + 3) + " " + (nbonds + 2) + " " + (nsubstructures + 1) + " " + 0 + " " + 0 + "\n");
               mol2outfile.write("SMALL" + "\n");
               mol2outfile.write("NO_CHARGE" + "\n");
               mol2outfile.write("##solvndx  " + solvatedAtomIndices[0] + "  " + solvatedAtomIndices[1] + "\n");
               mol2outfile.write("\n");

               // Write coordinates section
               mol2outfile.write("@<TRIPOS>ATOM" + "\n");

               spc = "";  spc1 = "    "; spc2 = "     ";

               // Add coordinates of solvated molecule               
               for (ndx = 0; ndx < natoms; ndx++)
               {
            //        spc = "";  spc1 = "    "; spc2 = "     ";

                    if (ndx < 9)
                    {
                         spc = " ";
                    }

                    if ((atomNames.get(ndx)).length() > 2)
                    {
                         spc2 = "    ";
                    }

                    mol2outfile.write(spc + (ndx+1) + spc1 + atomNames.get(ndx) + spc2 + atomCoordinates[ndx][1] + "    ");
                    mol2outfile.write(atomCoordinates[ndx][2] + "    " + atomCoordinates[ndx][3] + "    ");
                    mol2outfile.write(elemID.get(ndx) + "    " + (int)atomCoordinates[0][4] + "\n");
               }

               noxygens = nhydrogens = 0;

               for (ndx = 0; ndx < natoms; ndx++)
               {
                    elemid = elemID.get(ndx);

                    if (elemid.equals("O"))
                    {
                         noxygens++;
                    }
                    else if (elemid.equals("H"))
                    {
                         nhydrogens++;
                    }
               }

               // Add coordinates of solvent (O, H, H)
               mol2outfile.write(spc + (natoms + 1) + spc1 + "O" + (noxygens + 1) + spc2 + solventCoordinates[0][0] + "    ");               
               mol2outfile.write(solventCoordinates[0][1] + "    " + solventCoordinates[0][2] + "    ");
               mol2outfile.write("O" + "    " + (nsubstructures + 1) + "\n");
  
               mol2outfile.write(spc + (natoms + 2) + spc1 + "H" + (nhydrogens + 1) + spc2 + solventCoordinates[1][0] + "    ");               
               mol2outfile.write(solventCoordinates[1][1] + "    " + solventCoordinates[1][2] + "    ");
               mol2outfile.write("H" + "    " + (nsubstructures + 1) + "\n");

               mol2outfile.write(spc + (natoms + 3) + spc1 + "H" + (nhydrogens + 2) + spc2 + solventCoordinates[2][0] + "    ");               
               mol2outfile.write(solventCoordinates[2][1] + "    " + solventCoordinates[2][2] + "    ");
               mol2outfile.write("H" + "    " + (nsubstructures + 1) + "\n");

               // Write connectivity section
               mol2outfile.write("@<TRIPOS>BOND" + "\n");

               indx = 0;

               for (j = 0; j < natoms; j++)
               {
                    for (k = (j+1); k < natoms; k++)
                    {
                         if (atomConnectivity[j][k] > 0)
                         {
                              indx++;  spc = "";  spc1 = "    ";

                              if (indx < 10)
                              {
                                   spc = " ";
                              }

                              mol2outfile.write(spc + indx + spc1 + (j+1) + "   " + (k+1) + "   " + atomConnectivity[j][k] + "\n");
                         }
                    }
               }

               // Add solvent O-H bonds 

               mol2outfile.write(spc + (nbonds + 1) + spc1 + (natoms + 1) + "   " + (natoms + 2) + "   " + 1 + "\n");
               mol2outfile.write(spc + (nbonds + 1) + spc1 + (natoms + 1) + "   " + (natoms + 3) + "   " + 1 + "\n");

               mol2outfile.close();

//               solvinputfile = solvatedAtomsDir + "\\" + molName + ".mol2";
               solvinputfile = solvatedAtomsDir + fsep + molName + ".mol2";

               theta = getTheta();  phi = getPhi();  psi = getPsi();

               readCoordsFile(solvinputfile);

               glcanvas.repaint();
          }
          catch (IOException e)
          {
               e.printStackTrace();
          } 
     }

     public void computeBondAngle(int centralAtomNdx, ArrayList<Integer> bondedAtoms)
     {
          int ndx1, ndx2, ndx3, end1Ndx, end2Ndx;
          double dx1, dx2, dy1, dy2, dz1, dz2, D1, D2;
          ArrayList<Integer> endAtoms = new ArrayList<Integer>();

          atomCoordinates = getCoordinates();

          ndx1 = bondedAtoms.get(0);  ndx2 = bondedAtoms.get(1);  //ndx3 = bondedAtoms.get(2);  

          for (ndx = 0; ndx < bondedAtoms.size(); ndx++)
          {
               if (bondedAtoms.get(ndx) != centralAtomNdx)
               {
                    endAtoms.add(bondedAtoms.get(ndx));
               }
          }

          end1Ndx = endAtoms.get(0);  end2Ndx = endAtoms.get(1);
      
          dx1 = atomCoordinates[end1Ndx][1] - atomCoordinates[centralAtomNdx][1];
          dy1 = atomCoordinates[end1Ndx][2] - atomCoordinates[centralAtomNdx][2];
          dz1 = atomCoordinates[end1Ndx][3] - atomCoordinates[centralAtomNdx][3];

          dx2 = atomCoordinates[end2Ndx][1] - atomCoordinates[centralAtomNdx][1];
          dy2 = atomCoordinates[end2Ndx][2] - atomCoordinates[centralAtomNdx][2];
          dz2 = atomCoordinates[end2Ndx][3] - atomCoordinates[centralAtomNdx][3];

          D1 = Math.sqrt(dx1*dx1 + dy1*dy1 + dz1*dz1);  D2 = Math.sqrt(dx2*dx2 + dy2*dy2 + dz2*dz2);

          // Compute unit vector components
          dx1 = dx1/D1; dy1 = dy1/D1; dz1 = dz1/D1;     dx2 = dx2/D2; dy2 = dy2/D2; dz2 = dz2/D2;

          // Compute bond angle from dot product of the unit vectors
          bondAngle = (180.0/Math.PI) * Math.acos(dx1*dx2 + dy1*dy2 + dz1*dz2); 

          strBondAngle = Double.toString(bondAngle);
          int pointNdx = strBondAngle.indexOf(".");

          strlngth = Math.min( (strBondAngle.length() - pointNdx), pointNdx+3); 
          strBondAngle = strBondAngle.substring(0, strlngth);
     }

     public double getBondAngle()
     {
          return bondAngle;
     }

     public String getStrBondAngle()
     {
          return strBondAngle;
     }     

     public String getStrDihedralAngle()
     {
          return strDihedralAngle;
     }     

     public String getHybridization()
     {
          return hybridization;
     }

     public double setDihedralAngle(ArrayList<Double> bvvec1, ArrayList<Double> bvvec2, ArrayList<Double> axvec)
     {
          int ndx11, ndx12, ndx21, ndx22;
          double nx, ny, nz, Dn, nxhat, nyhat, nzhat;
          double dx1, dxc, dx2, dy1, dyc, dy2, dz1, dzc, dz2, D1, Dc, D2, DR1, DR2, dot12;
          double r1x, r2x, r1y, r2y, r1z, r2z;
 
          dx1 = bvvec1.get(0);  dy1 = bvvec1.get(1); dz1 = bvvec1.get(2);
          dx2 = bvvec2.get(0);  dy2 = bvvec2.get(1); dz2 = bvvec2.get(2);
          dxc = axvec.get(0);  dyc = axvec.get(1); dzc = axvec.get(2);

          // Compute unit vectors normal to each pair of contiguous axes
          r1x = dy1*dzc - dz1*dyc;  r1y = dz1*dxc - dx1*dzc; r1z = dx1*dyc - dy1*dxc; 
          DR1 = Math.sqrt(r1x*r1x + r1y*r1y + r1z*r1z);
          r1x = r1x/DR1;  r1y = r1y/DR1; r1z = r1z/DR1;          

          r2x = dy2*dzc - dz2*dyc;  r2y = dz2*dxc - dx2*dzc; r2z = dx2*dyc - dy2*dxc; 
          DR2 = Math.sqrt(r2x*r2x + r2y*r2y + r2z*r2z);
          r2x = r2x/DR2;  r2y = r2y/DR2; r2z = r2z/DR2;

          // Compute dot product of the above two unit vectors
          dot12 = r1x*r2x + r1y*r2y + r1z*r2z;
          
          // Compute dihedral angle       
          dihedralAngle = (180.0/Math.PI) * Math.acos(dot12); 

          // Determine if dihedral angle is positive or negative
          
          // Compute direction of unit normal axial vector from the cross product of r1 with r2
          nx = r1y*r2z - r1z*r2y;  ny = r1z*r2x - r1x*r2z;  nz = r1x*r2y - r1y*r2x;
          Dn = Math.sqrt(nx*nx + ny*ny + nz*nz);
          nxhat = nx/Dn;  nyhat = ny/Dn;  nzhat = nz/Dn;

          if (( (nxhat*dxc) + (nyhat*dyc) + (nzhat*dzc) ) > 0) 
          {
               dihedralAngle = -dihedralAngle;
          }

          return dihedralAngle;
     }

     public void computeDihedralAngle(ArrayList<Integer> selectedComponents)
     {
          int ndx11, ndx12, ndx21, ndx22;
          double nx, ny, nz, Dn, nxhat, nyhat, nzhat;
          double dx1, dxc, dx2, dy1, dyc, dy2, dz1, dzc, dz2, D1, Dc, D2, DR1, DR2, dot12;
          double r1x, r2x, r1y, r2y, r1z, r2z;

          dx1 = dy1 = dz1 = 0.0;  dx2 = dy2 = dz2 = 0.0;

          atomCoordinates = getCoordinates();

          ndx11 = selectedComponents.get(0);  ndx12 = selectedComponents.get(1);
          ndx21 = selectedComponents.get(2);  ndx22 = selectedComponents.get(3);

          if (ndx11 >= 0)
          {
               // Compute direction along bond from axisAtom1 to nonAxisAtom1
               dx1 = atomCoordinates[ndx11][1] - atomCoordinates[ndx12][1];
               dy1 = atomCoordinates[ndx11][2] - atomCoordinates[ndx12][2];
               dz1 = atomCoordinates[ndx11][3] - atomCoordinates[ndx12][3];
          }
          else
          {
               for (indx = 0; indx < bondVectors.length; indx++)
               {
                    if (bondVectors[indx][0] == ndx11)
                    {
                         dx1 = bondVectors[indx][1];  dy1 = bondVectors[indx][2];  dz1 = bondVectors[indx][3]; 

                         break;
                    }
               }
          }

          // Compute direction along dihedral rotation axis from axisAtom1 to axisAtom2
          dxc = atomCoordinates[ndx21][1] - atomCoordinates[ndx12][1];
          dyc = atomCoordinates[ndx21][2] - atomCoordinates[ndx12][2];
          dzc = atomCoordinates[ndx21][3] - atomCoordinates[ndx12][3];

          if (ndx22 >= 0)
          {
               // Compute direction along bond from axisAtom2 to nonAxisAtom2
               dx2 = atomCoordinates[ndx22][1] - atomCoordinates[ndx21][1];
               dy2 = atomCoordinates[ndx22][2] - atomCoordinates[ndx21][2];
               dz2 = atomCoordinates[ndx22][3] - atomCoordinates[ndx21][3];
          }
          else
          {
               for (indx = 0; indx < bondVectors.length; indx++)
               {
                    if (bondVectors[indx][0] == ndx22)
                    {
                         dx2 = bondVectors[indx][1];  dy2 = bondVectors[indx][2];  dz2 = bondVectors[indx][3]; 

                         break;
                    }
               }
          }

          // Compute unit vectors along each axial direction
          D1 = Math.sqrt(dx1*dx1 + dy1*dy1 + dz1*dz1);  
          Dc = Math.sqrt(dxc*dxc + dyc*dyc + dzc*dzc);
          D2 = Math.sqrt(dx2*dx2 + dy2*dy2 + dz2*dz2);   

          if ( (D1 > 0) && (Dc > 0) && (D2 > 0) )
          {
               dx1 = dx1/D1;  dy1 = dy1/D1;  dz1 = dz1/D1;
               dxc = dxc/Dc;  dyc = dyc/Dc;  dzc = dzc/Dc;
               dx2 = dx2/D2;  dy2 = dy2/D2;  dz2 = dz2/D2;

               // Compute unit vectors normal to each pair of contiguous axes
               r1x = dy1*dzc - dyc*dz1;  r1y = dxc*dz1 - dx1*dzc; r1z = dx1*dyc - dxc*dy1; 
               DR1 = Math.sqrt(r1x*r1x + r1y*r1y + r1z*r1z);
               r1x = r1x/DR1;  r1y = r1y/DR1; r1z = r1z/DR1;

               r2x = dy2*dzc - dyc*dz2;  r2y = dxc*dz2 - dx2*dzc; r2z = dx2*dyc - dxc*dy2; 
               DR2 = Math.sqrt(r2x*r2x + r2y*r2y + r2z*r2z);
               r2x = r2x/DR2;  r2y = r2y/DR2; r2z = r2z/DR2;

               // Compute dot product of the above two unit vectors
               dot12 = r1x*r2x + r1y*r2y + r1z*r2z;
          
               // Compute dihedral angle       
               dihedralAngle = (180.0/Math.PI) * Math.acos(dot12); 

               // Determine if dihedral angle is positive or negative
          
               // Compute direction of unit normal axial vector from the cross product of r1 with r2
               nx = r1y*r2z - r2y*r1z;  ny = r2x*r1z - r1x*r2z;  nz = r1x*r2y - r2x*r1y;
               Dn = Math.sqrt(nx*nx + ny*ny + nz*nz);
               nxhat = nx/Dn;  nyhat = ny/Dn;  nzhat = nz/Dn;

               if (( (nxhat*dxc) + (nyhat*dyc) + (nzhat*dzc) ) > 0) 
               {
                    dihedralAngle = -dihedralAngle;
               }

               strDihedralAngle = Double.toString(dihedralAngle);
               int pointNdx = strDihedralAngle.indexOf(".");

               strlngth = Math.min( (strDihedralAngle.length() - pointNdx), pointNdx+3); 
               strDihedralAngle = strDihedralAngle.substring(0, strlngth);
          }
          else
          {
               strDihedralAngle = "N/A";
          }

          dihedralAngleField.setText(strDihedralAngle);

          strBondAngle = " ";
     }

     public double getDihedralAngle()
     {
          return dihedralAngle;
     }

     public double[][] getRotatedCoordinates(double[][] atomCoordinates, ArrayList<Integer> selectedFragmentArray, double angle)
     {

          return atomCoordinates;
     }

     public void getSelectedComponent(int natoms, ArrayList<Integer> selectedNames)
     {
          int name, atomNdx, bvAtomNdx, centralAtomNdx, end1Ndx, end2Ndx;
          int natomsSelected, nbondsSelected, nbvsSelected;
          
          ArrayList<Integer> bondedAtoms = new ArrayList<Integer>();
          ArrayList<Integer> atomsSelected = new ArrayList<Integer>();          
          ArrayList<Integer> bvsSelected = new ArrayList<Integer>();
          ArrayList<Integer> firstBondAtoms = new ArrayList<Integer>();
          ArrayList<Integer> centralBondAtoms = new ArrayList<Integer>();
          ArrayList<Integer> thirdBondAtoms = new ArrayList<Integer>();

          bvAtomNdx = centralAtomNdx = end1Ndx = end2Ndx = -1;
          natomsSelected = nbondsSelected = nbvsSelected = 0;

          strBondAngle = strDihedralAngle = "";

          for (ndx = 0; ndx < selectedNames.size(); ndx++)
          {
               name = selectedNames.get(ndx);

               if ( (name >= 0) && (name < natoms) )
               {
                    natomsSelected++;

                    atomsSelected.add(name);
               }
               else if (name >= 1000000)
               {
                    nbondsSelected++;

                    bondedAtoms.add(((int)(name/1000000) - 1));
                    bondedAtoms.add(name % 1000000);
               }
               else if (name <= (-100))
               {
                    nbvsSelected++;

                    bvsSelected.add(name);
               }
          }
 
          if (selectedNames.size() == 3)
          {
               if (natomsSelected == 3)
               {
                    bondedAtoms.add(selectedNames.get(0));
                    centralAtomNdx = selectedNames.get(1);
                    bondedAtoms.add(selectedNames.get(2));

                    rotType = 1;

                    // Get bond angle defined by the three selected atoms
//                    computeBondAngle(centralAtomNdx, bondedAtoms);
               }
               else if (nbondsSelected == 2)
               {
                    if (natomsSelected == 1)
                    {
                         atomNdx = atomsSelected.get(0);

                         if (bondedAtoms.contains(atomNdx))
                         {
                              rotType = 1;

//                              computeBondAngle(atomNdx, bondedAtoms);
                         }
                    }
               }
               else if (nbondsSelected == 3)
               {
                    firstBondAtoms.add(bondedAtoms.get(0));  firstBondAtoms.add(bondedAtoms.get(1));
                    centralBondAtoms.add(bondedAtoms.get(2));  centralBondAtoms.add(bondedAtoms.get(3));                     
                    thirdBondAtoms.add(bondedAtoms.get(4));  thirdBondAtoms.add(bondedAtoms.get(5));

                    if ( (centralBondAtoms.contains(firstBondAtoms.get(0))) || (centralBondAtoms.contains(firstBondAtoms.get(1))) )
                    {
                         if ( (centralBondAtoms.contains(thirdBondAtoms.get(0))) || (centralBondAtoms.contains(thirdBondAtoms.get(1))) )
                         { 
//                              computeDihedralAngle(firstBondAtoms, centralBondAtoms, thirdBondAtoms);
                         }
                    }
               }
          }
     }               

     public void setAtomNames(ArrayList<String> elemID)
     {
          int number, lastIndex, listIndex;
          String atmName, currentName, previousName;

          orderedAtomList.clear();
          orderedAtomListIndices.clear();
       
          for (ndx = 0; ndx < elemID.size(); ndx++)
          {
               atomNames.add("-1");

               atmName = elemID.get(ndx);

               if (orderedAtomList.contains(atmName))
               {
                    lastIndex = orderedAtomList.lastIndexOf(atmName);
                    orderedAtomList.add((lastIndex + 1), atmName);
                    orderedAtomListIndices.add((lastIndex + 1), ndx);
               }
               else
               {
                    orderedAtomList.add(atmName);
                    orderedAtomListIndices.add(ndx);
               }
          }

          number = 1;

          if (orderedAtomList.size() > 0)
          {
               atmName = orderedAtomList.get(0) + Integer.toString(number);

               atomNames.set(0, atmName);           

               for (ndx = 1; ndx < orderedAtomList.size(); ndx++)
               {
                    currentName = orderedAtomList.get(ndx);
                    previousName = orderedAtomList.get((ndx - 1));

                    if (currentName.equals(previousName))
                    {
                         number++;
                    }
                    else
                    {
                         number = 1;
                    }

                    atmName = currentName + Integer.toString(number);                    

                    listIndex = orderedAtomListIndices.get(ndx);
                    atomNames.set(listIndex, atmName); 
               }
          }
     }

     public void setFragmentOrigin(double[][] atomCoordinates, ArrayList<Integer> selectedFragmentAtoms)
     {
          double minX, maxX, minY, maxY, minZ, maxZ;

          indx = selectedFragmentAtoms.get(0);

          minX = maxX = atomCoordinates[indx][1];
          minY = maxY = atomCoordinates[indx][2];
          minZ = maxZ = atomCoordinates[indx][3];

          for (j = 1; j < selectedFragmentAtoms.size(); j++)
          {
               indx = selectedFragmentAtoms.get(j);
 
               minX = Math.min(minX, atomCoordinates[indx][1]);
               maxX = Math.max(maxX, atomCoordinates[indx][1]);

               minY = Math.min(minY, atomCoordinates[indx][2]);
               maxY = Math.max(maxY, atomCoordinates[indx][2]);

               minZ = Math.min(minZ, atomCoordinates[indx][3]);
               maxZ = Math.max(maxZ, atomCoordinates[indx][3]);
          }

          fragmentOrigin.clear();

          fragmentOrigin.add( (minX + maxX)/2 );
          fragmentOrigin.add( (minY + maxY)/2 );
          fragmentOrigin.add( (minZ + maxZ)/2 );
     }

     public boolean isRingCarbon(int hbDonorNdx, int nnNdx)
     {  
          // Determine if carbon ato  m nearest-neighbor is part of a ring system (a ring carbon)

          // Get nearest-neighbors to C atom excluding H-bond donor atom
          int listNdx = 0;
          int nNN = 0;

          int[][] tmpConnectivity;
          int[][] tmpConnections;

          boolean ringCarbon;

          ArrayList <Integer> carbonNN = new ArrayList <Integer>();
                                                                                              
          ringCarbon = false;

          tmpConnectivity = new int[natoms][natoms];
 
          for (j = 0; j < natoms; j++)
          {
               for (k = 0; k < natoms; k++)
               {
                    tmpConnectivity[j][k] = atomConnectivity[j][k];
               } 
          }

          tmpConnectivity[hbDonorNdx][nnNdx] = tmpConnectivity[nnNdx][hbDonorNdx] = 0;

          for (ndx = 0; ndx < natoms; ndx++)
          {
               if (tmpConnectivity[nnNdx][ndx] > 0)
               {
                   carbonNN.add(ndx);
               }  
          }

          if (carbonNN.size() == 1)       // triple-bonded carbon => can't be member of a ring system
          {  
               ringCarbon = false;
          }
          else if (carbonNN.size() > 1)   // 2 nn atoms (1 double bond and 1 single bond)  OR  3 nn atoms (3 single bonds) 
          { 
               ArrayList <Integer> connectionList1 = new ArrayList <Integer>();
               ArrayList <Integer> connectionList2 = new ArrayList <Integer>();
               ArrayList <Integer> connectionList3 = new ArrayList <Integer>();;

               connectionList1.add(nnNdx);  connectionList2.add(nnNdx);

               // Populate connectionList1
               tmpConnections = new int[natoms][natoms];

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < natoms; k++)
                    { 
                         tmpConnections[j][k] = tmpConnectivity[j][k];
                    }
               }

               // Delete bond between C atom and first atom in carbon nearest-neighbor list
               tmpConnections[carbonNN.get(0)][nnNdx] = tmpConnections[nnNdx][carbonNN.get(0)] = 0;

               listNdx = 0;

               while (listNdx < connectionList1.size())
               {
                    indx = connectionList1.get(listNdx);
                                                            
                    for (k = 0; k < natoms; k++)
                    {
                         if ( (tmpConnections[indx][k] > 0) && (!connectionList1.contains(k)) )
                         {
                              connectionList1.add(k);
                         }                                                            
                    }

                    listNdx++;
               }

               // Populate connectionList2
               tmpConnections = new int[natoms][natoms];

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < natoms; k++)
                    {
                         tmpConnections[j][k] = tmpConnectivity[j][k];
                    }
               }

              // Delete bond between C atom and second atom in carbon nearest-neighbor list
               tmpConnections[carbonNN.get(1)][nnNdx] = tmpConnections[nnNdx][carbonNN.get(1)] = 0;

               listNdx = 0;

               while (listNdx < connectionList2.size())
               {
                    indx = connectionList2.get(listNdx);
                                                            
                    for (k = 0; k < natoms; k++)
                    {
                         if ( (tmpConnections[indx][k] > 0) && (!connectionList2.contains(k)) )
                         {
                              connectionList2.add(k);
                         }                                                            
                    }

                    listNdx++;
               }

               if (carbonNN.size() == 3)      // 3 nn atoms (3 single bonds)
               {
                    connectionList3.add(nnNdx);

                    // Populate connectionList3
                    tmpConnections = new int[natoms][natoms];

                    for (j = 0; j < natoms; j++)
                    {
                         for (k = 0; k < natoms; k++)
                         {
                              tmpConnections[j][k] = tmpConnectivity[j][k];
                         }
                    }

                    // Delete bond between C atom and third atom in carbon nearest-neighbor list
                    tmpConnections[carbonNN.get(2)][nnNdx] = tmpConnections[nnNdx][carbonNN.get(2)] = 0;
 
                    listNdx = 0;

                    while (listNdx < connectionList3.size())
                    {
                         indx = connectionList3.get(listNdx);
                                                            
                         for (k = 0; k < natoms; k++)
                         {
                              if ( (tmpConnections[indx][k] > 0) && (!connectionList3.contains(k)) )
                              {
                                   connectionList3.add(k);
                              }                                                            
                         }
  
                         listNdx++;
                    }
               }       
  
               // Compare indices in connection lists

  
               // Compare connection lists 1 and 2
               // Atom list cannot be identical if the lengths of the two lists are not identical
               if (connectionList1.size() == connectionList2.size())
               {
                    ringCarbon = true;
 
                    for (ndx = 0; ndx < connectionList1.size(); ndx++)
                    {
                         if (!connectionList2.contains(connectionList1.get(ndx)))
                         {
                              ringCarbon = false;
                         }
                    }
               }

               // If C atom has 3 single bonds to nearest-neighbos and connection lists 1 and 2 are not identical, compare connection lists 1 and 3
               if ( (carbonNN.size() == 3) && (!ringCarbon) )
               {
                    if (connectionList1.size() == connectionList3.size())
                    {
                         ringCarbon = true;
 
                         for (ndx = 0; ndx < connectionList1.size(); ndx++)
                         {
                              if (!connectionList3.contains(connectionList1.get(ndx)))
                              {
                                   ringCarbon = false;
                              }
                         }
                    }
                                                              
                    // If connection lists 1 and 2 are not identical, compare connection lists 2 and 3
                    if (!ringCarbon)
                    {
                         // Atom list cannot be identical if the lengths of the two lists are not identical
                         if (connectionList2.size() == connectionList3.size())
                         { 
                              ringCarbon = true;
 
                              for (ndx = 0; ndx < connectionList2.size(); ndx++)
                              {
                                   if (!connectionList3.contains(connectionList2.get(ndx)))
                                   {
                                        ringCarbon = false;
                                   }
                              }
                         }
                    }
               }
          }

          return ringCarbon;
     }

     public boolean hasElectronegativeNeighbor(int hbDonorNdx, int nnNdx)
     {
          boolean CnnEN, CnnnEN;     // carbon nearest-neighbor and carbon next nearest-neighbor
          boolean electronegativeNeighbor;

          ArrayList<Integer> nnnNdx = new ArrayList<Integer>();

          CnnEN = CnnnEN = false;
          electronegativeNeighbor = false;

          for (ndx = 0; ndx < natoms; ndx++)
          {
               if ( (atomConnectivity[nnNdx][ndx] > 0) && (ndx != hbDonorNdx) )
               {     
                    if (Hacceptors.contains(elemID.get(ndx)))
                    {
                         nnnNdx.add(ndx);

                         CnnEN = true;

                         break;
                    }
               }
          }

          if (!CnnEN)
          {
               for (j = 0; j < nnnNdx.size(); j++)
               {
                    for (k = 0; k < natoms; k++)
                    {
                         if ( (atomConnectivity[j][k] > 0)  && (k != nnNdx) )
                         {
                              if (Hacceptors.contains(elemID.get(k)))
                              {
                                   CnnnEN = true;

                                   break;
                              }
                         }
                    }
               }
          }

          if ( (CnnEN) || (CnnnEN) )
          {
               electronegativeNeighbor = true;
          }

          return electronegativeNeighbor;
     }

     public void solvateHdonor(double[][] hbAxis, int targetAtomNdx, int nnNdx, int nnnNdx) 
     {
          int orientNdx;
          int nsolventAtoms = 3;
          int norientations = 3;
          
          double dx1, dy1, dz1, dx2, dy2, dz2, dx3, dy3, dz3;
          double dx, dy, dz, sx, sy, sz, sx_star, sy_star, sz_star; 
          double tx, ty, tz, bx, by, bz, r11, r22, r33, Dr;
          double lpx, lpy, lpz, lpx_star, lpy_star, lpz_star;
          double mdhx, mdhy, mdhz, dHHlpx, dHHlpy, dHHlpz, sdhx, sdhy, sdhz;
          double n11, n22, n33;
          double dhx, dhy, dhz, scrax, scray, scraz, dHHx, dHHy, dHHz, R;       // directional vectors for staggered conformation reference axes
          double delsx, delsy, delsz;
          double angle, cosangle, sinangle, delangle;
          double msdhx, msdhy, msdhz, msdhdotlp;
          double defaultHBLength;

          double[] solventAxis = new double[ncoords];
          double[] solventRefAxis = new double[ncoords];
          double[] staggeredConformationRefAxis = new double[ncoords];

          double[][] mdhAxis;
          double[][] sdhAxis;
          double[][] nn2nnnAxis;
          double[][] rotMatrix = new double[ncoords][ncoords];
          double[][] waterCoordinates;

          // O1-H1-H2 coordinates
          double[][] solventH2OCoordinates = { {0.0, 0.0, 0.0}, {-0.52953, 0.320158, 0.731866}, {-0.38729, 0.411104, -0.774290} };

          // Coordinates of one LP on the O atom of H2O
          double[] oxygenLPAxis = {0.98372, 0.132314, 0.121611};

          String molName, solvAtomInputFile; 

          File solvAtomFile;

          solventOrientNum = 0;

          boolean parallelVecs = false;

          cosangle = sinangle = 0.0;
     
          tx = hbAxis[0][0];  ty = hbAxis[0][1];  tz = hbAxis[0][2];
          lpx = lpy = lpz = bx = by = bz = 0.0;

          defaultHBLength = 2.1;

          smonMax = 3;

          lpx = oxygenLPAxis[0];  lpy = oxygenLPAxis[1];  lpz = oxygenLPAxis[2];

          // Get unit vector antiparallel to target axis 
          tx = -tx;  ty = -ty;  tz = -tz;

          r11 = ty*lpz - tz*lpy;  r22 = tz*lpx - tx*lpz;  r33 = tx*lpy - ty*lpx;

          Dr = Math.sqrt( (r11*r11) + (r22*r22) + (r33*r33) );

          // Orient solvent molecule w.r.t. H-bond donor axis
          cosangle = tx*lpx + ty*lpy + tz*lpz;
          sinangle = Math.sin(Math.acos(cosangle));

          parallelVecs = false;

          if (Dr < 0.000010)
          {
               parallelVecs = true;

               if (cosangle < 0)
               {
                    // Reflect solventH2OCoordinates through the origin
                    for (ndx = 0; ndx < nsolventAtoms; ndx++)
                    {
                         solventH2OCoordinates[ndx][0] = (-solventH2OCoordinates[ndx][0]);
                         solventH2OCoordinates[ndx][1] = (-solventH2OCoordinates[ndx][1]);
                         solventH2OCoordinates[ndx][2] = (-solventH2OCoordinates[ndx][2]);
                    }

                    // Reflect oxygen LP axis through the origin
                    lpx = -lpx;  lpy = -lpy;  lpz = -lpz;
               }
          }
          else
          {
               n11 = r11/Dr;  n22 = r22/Dr;  n33 = r33/Dr;

               // Rotate solvent molecule to bring oxygen LP anti|| to H-bond donor axis
               rotMatrix[0][0] = (n11 * n11) + (cosangle * (1 - n11 * n11));
               rotMatrix[0][1] = ((n11 * n22) * (1 - cosangle)) + (n33 * sinangle);
               rotMatrix[0][2] = ((n33 * n11) * (1 - cosangle)) - (n22 * sinangle);

               rotMatrix[1][0] = ((n11 * n22) * (1 - cosangle)) - (n33 * sinangle);
               rotMatrix[1][1] = (n22 * n22) + (cosangle * (1 - n22 * n22));
               rotMatrix[1][2] = ((n22 * n33) * (1 - cosangle)) + (n11 * sinangle);
 
               rotMatrix[2][0] = ((n33 * n11) * (1 - cosangle)) + (n22 * sinangle);
               rotMatrix[2][1] = ((n22 * n33) * (1 - cosangle)) - (n11 * sinangle);
               rotMatrix[2][2] = (n33 * n33) + (cosangle * (1 - n33 * n33));

               // Compute rotated solvent coordinates
               for (ndx = 0; ndx < nsolventAtoms; ndx++)
               {               
                    sx = solventH2OCoordinates[ndx][0];  sy = solventH2OCoordinates[ndx][1];  sz = solventH2OCoordinates[ndx][2];
      
                    sx_star = rotMatrix[0][0]*sx + rotMatrix[0][1]*sy + rotMatrix[0][2]*sz;
                    sy_star = rotMatrix[1][0]*sx + rotMatrix[1][1]*sy + rotMatrix[1][2]*sz;
                    sz_star = rotMatrix[2][0]*sx + rotMatrix[2][1]*sy + rotMatrix[2][2]*sz;

                    solventH2OCoordinates[ndx][0] = sx_star;
                    solventH2OCoordinates[ndx][1] = sy_star;
                    solventH2OCoordinates[ndx][2] = sz_star; 
               }

               // Compute rotated oxygen LP axis
               lpx_star = rotMatrix[0][0]*lpx + rotMatrix[0][1]*lpy + rotMatrix[0][2]*lpz;
               lpy_star = rotMatrix[1][0]*lpx + rotMatrix[1][1]*lpy + rotMatrix[1][2]*lpz;
               lpz_star = rotMatrix[2][0]*lpx + rotMatrix[2][1]*lpy + rotMatrix[2][2]*lpz;

               lpx = lpx_star;  lpy = lpy_star;  lpz = lpz_star;
          }

          // Compute dihedral angle axes and obtain a parallel alignment
 
          dx1 = hbAxis[0][0];  dy1 = hbAxis[0][1];  dz1 = hbAxis[0][2];

          // Compute NN -> NNN axis direction
          dx2 = atomCoordinates[nnnNdx][1] - atomCoordinates[nnNdx][1];
          dy2 = atomCoordinates[nnnNdx][2] - atomCoordinates[nnNdx][2];
          dz2 = atomCoordinates[nnnNdx][3] - atomCoordinates[nnNdx][3];

          R = Math.sqrt(dx2*dx2 + dy2*dy2 + dz2*dz2);

          dx2 = dx2/R;  dy2 = dy2/R;  dz2 = dz2/R;

          // Determine direction of normal to H-bond axis and NN -> NNN axis (dr2 x dr1)
          dx3 = dy2*dz1 - dy1*dz2;  dy3 = dz2*dx1 - dz1*dx2;  dz3 = dx2*dy1 - dx1*dy2;

          R = Math.sqrt(dx3*dx3 + dy3*dy3 + dz3*dz3);

          dx3 = dx3/R;  dy3 = dy3/R;  dz3 = dz3/R;

          // Determine direction of normal to H-bond axis and dr3 axis (dr3 x dr1)
          mdhx = dy3*dz1 - dy1*dz3;  mdhy = dz3*dx1 - dz1*dx3;  mdhz = dx3*dy1 - dx1*dy3;
          R = Math.sqrt(mdhx*mdhx + mdhy*mdhy + mdhz*mdhz);

          mdhx = mdhx/R;  mdhy = mdhy/R;  mdhz = mdhz/R;

          // Compute direction of reference axis bisecting H atoms on H2O solvent molecule
          dHHx = ((solventH2OCoordinates[1][0] + solventH2OCoordinates[2][0]) - solventH2OCoordinates[0][0]);
          dHHy = ((solventH2OCoordinates[1][1] + solventH2OCoordinates[2][1]) - solventH2OCoordinates[0][1]);
          dHHz = ((solventH2OCoordinates[1][2] + solventH2OCoordinates[2][2]) - solventH2OCoordinates[0][2]);
    
          R = Math.sqrt( (dHHx*dHHx) + (dHHy*dHHy) + (dHHz*dHHz) );

          dHHx = dHHx/R;  dHHy = dHHy/R;  dHHz = dHHz/R;

          // Compute cross-product of dHH axis and LP axis (dHH x LP)
          dHHlpx = dHHy*lpz - dHHz*lpy;  dHHlpy = dHHz*lpx - dHHx*lpz;  dHHlpz = dHHx*lpy - dHHy*lpx;

          R = Math.sqrt( (dHHlpx*dHHlpx) + (dHHlpy*dHHlpy) + (dHHlpz*dHHlpz) );

          dHHlpx = dHHlpx/R;  dHHlpy = dHHlpy/R;  dHHlpz = dHHlpz/R;

          // Compute normal to LP and dHHlp axes (LP x dHHlp)
          sdhx = lpy*dHHlpz - lpz*dHHlpy;  sdhy = lpz*dHHlpx - lpx*dHHlpz;  sdhz = lpx*dHHlpy - lpy*dHHlpx;

          R = Math.sqrt( (sdhx*sdhx) + (sdhy*sdhy) + (sdhz*sdhz) );

          sdhx = sdhx/R;  sdhy = sdhy/R;  sdhz = sdhz/R;

          msdhx = mdhy*sdhz - mdhz*sdhy;  msdhy = mdhz*sdhx - mdhx*sdhz;  msdhz = mdhx*sdhy - mdhy*sdhx;

          R = Math.sqrt(msdhx*msdhx + msdhy*msdhy + msdhz*msdhz);

          msdhx = msdhx/R;  msdhy = msdhy/R;  msdhz = msdhz/R;

          msdhdotlp = msdhx*lpx + msdhy*lpy + msdhz*lpz;

          cosangle = mdhx*sdhx + mdhy*sdhy + mdhz*sdhz;          

          delangle = (180/Math.PI)*Math.acos(cosangle);

          if (msdhdotlp > 0)
          {
               delangle = Math.abs(delangle) + 60;
          }
          else if (msdhdotlp < 0)
          {
               delangle = -Math.abs(delangle) + 60;
          }

          // Align mdhAxis and sdhAxis by rotating about H-bond axis by delangle, then rotate twice in sequence by 120 degrees
          n11 = lpx;  n22 = lpy;  n33 = lpz;

          waterCoordinates = new double[nsolventAtoms][ncoords];

          tx = -tx;  ty = -ty;  tz = -tz;

          delsx = solventH2OCoordinates[0][0];  delsy = solventH2OCoordinates[0][1];  delsz = solventH2OCoordinates[0][2]; 

          for (orientNdx = 0; orientNdx < norientations; orientNdx++)
          {
               solventOrientNum++;

               angle = delangle + (120.0*solventOrientNum);

               cosangle = Math.cos((Math.PI/180)*angle);
               sinangle = Math.sin((Math.PI/180)*angle);

               rotMatrix[0][0] = (n11 * n11) + (cosangle * (1 - n11 * n11));
               rotMatrix[0][1] = ((n11 * n22) * (1 - cosangle)) + (n33 * sinangle);
               rotMatrix[0][2] = ((n33 * n11) * (1 - cosangle)) - (n22 * sinangle);
  
               rotMatrix[1][0] = ((n11 * n22) * (1 - cosangle)) - (n33 * sinangle);
               rotMatrix[1][1] = (n22 * n22) + (cosangle * (1 - n22 * n22));
               rotMatrix[1][2] = ((n22 * n33) * (1 - cosangle)) + (n11 * sinangle);
 
               rotMatrix[2][0] = ((n33 * n11) * (1 - cosangle)) + (n22 * sinangle);
               rotMatrix[2][1] = ((n22 * n33) * (1 - cosangle)) - (n11 * sinangle);
               rotMatrix[2][2] = (n33 * n33) + (cosangle * (1 - n33 * n33));

               // Compute rotated solvent coordinates for each of 3 possible orientations
               for (ndx = 0; ndx < nsolventAtoms; ndx++)
               {               
                    sx = solventH2OCoordinates[ndx][0] - delsx;  sy = solventH2OCoordinates[ndx][1] - delsy;  sz = solventH2OCoordinates[ndx][2] - delsz;
      
                    sx_star = rotMatrix[0][0]*sx + rotMatrix[0][1]*sy + rotMatrix[0][2]*sz;
                    sy_star = rotMatrix[1][0]*sx + rotMatrix[1][1]*sy + rotMatrix[1][2]*sz;
                    sz_star = rotMatrix[2][0]*sx + rotMatrix[2][1]*sy + rotMatrix[2][2]*sz;

                    waterCoordinates[ndx][0] = sx_star + delsx;
                    waterCoordinates[ndx][1] = sy_star + delsy;
                    waterCoordinates[ndx][2] = sz_star + delsz; 
               }  

               // Translate solvent atoms so that O atom coincides with H-bond donor atom; then translate solvent atoms along H-bond
               //   axis a distance equal to default H-bond length
               dx = atomCoordinates[targetAtomNdx][1] - solventH2OCoordinates[0][0];
               dy = atomCoordinates[targetAtomNdx][2] - solventH2OCoordinates[0][1];
               dz = atomCoordinates[targetAtomNdx][3] - solventH2OCoordinates[0][2];

               for (ndx = 0; ndx < nsolventAtoms; ndx++)
               {
                    waterCoordinates[ndx][0] += (dx + (tx*defaultHBLength));
                    waterCoordinates[ndx][1] += (dy + (ty*defaultHBLength));
                    waterCoordinates[ndx][2] += (dz + (tz*defaultHBLength));
               }

               molName = moleculeName + "_s" + atomNames.get(targetAtomNdx) + "_" + Integer.toString(solventOrientNum);

//               solvAtomFile = new File(paramchemDir + strSolvatedMoleculesDir + "\\" + moleculeName + "\\" + molName + ".mol2");
//               solvAtomFile = new File(paramchemDir + strSolvatedMoleculesDir + "\\" + moleculeName);
               solvAtomFile = new File(paramchemDir + strSolvatedMoleculesDir + fsep + moleculeName);

//               solvAtomInputFile = paramchemDir + strSolvatedMoleculesDir + "\\" + moleculeName + "\\" + molName + ".mol2";
//               solvAtomInputFile = solvAtomFile + "\\" + molName + ".mol2";
               solvAtomInputFile = solvAtomFile + fsep + molName + ".mol2";



               // Get the indices of the two atoms involved in the solvation interaction  
               solvatedAtomIndices[0] = targetAtomNdx;
               solvatedAtomIndices[1] = (natoms - nsolventAtoms);       // this will always equal the total number of atoms in the molecule being solvated

               writeSolvatedAtomFiles(molName, targetAtomNdx, waterCoordinates);
          }
     }




     public void solvateHacceptor(double[][] hbAxis, int hbAcceptorNdx, double[] hbrefAxis) 
     {


System.out.println("hbAcceptorNdx, atom being solvated: " + hbAcceptorNdx + " ,  " + elemID.get(hbAcceptorNdx));


     }




     public ArrayList<Double> getFragmentOrigin()
     {
          return fragmentOrigin;
     }

     public int getNatoms()
     {
          return natoms;
     }

     public int getNbonds()
     {
          nbonds = 0;

          for (j = 0; j < (natoms-1); j++)
          {
               for (k = (j+1); k < natoms; k++)
               {
                    if (atomConnectivity[j][k] > 0)
                    {
                         nbonds++;
                    }
               }
          }

          return nbonds;
     }

     public int getNfragatoms()
     {
          return nfragatoms;
     }

     public int getFragmentNum()
     {
          return fragmentNum;
     }

     public int getNsubstructures()
     {
          return nsubstructures;
     }

     public int getUserDefFileSuffix()
     {
          return userDefFileSuffix;
     }

     public ArrayList<String> getAtomNames()
     {
          return atomNames;
     }

     public ArrayList<String> getOrderedAtomList()
     {
          return orderedAtomList;
     }

     public ArrayList<Integer> getOrderedAtomListIndices()
     {
          return orderedAtomListIndices;
     }

     public int getNbondVectors()
     {
          return nbondVectors;
     }

     public int getNbvRows()
     {
          return nbvRows;
     }

     public double[][] getCoordinates()
     {
          return atomCoordinates;
     }

     public double[][] getFragCoordinates()
     {
          return fragatomCoordinates;
     }

     public int[][] getAtomConnectivity()
     {
          return atomConnectivity;
     }

     public int[][] getFragConnectivity()
     {
          return fragatomConnectivity;
     }

     public float[][] getElementRGB()
     {
          return elementRGB;
     }

     public float[][] getElementColors()
     {
          return RGB;
     } 

     public ArrayList<Float> getNewColor()
     {
          return newColor;
     }      

     public double getTheta()
     {
          return theta;
     }

     public double getPhi()
     {
          return phi;
     }

     public double getPsi()
     {
          return psi;
     }


public double getMux()
{
    return mux;
}

public double getMuy()
{
    return muy;
}

public double getMuz()
{
    return muz;
}




     public double[] getMouseDownCoords()
     {
          return mouseDownCoords;
     }

     public double[] getMouseUpCoords()
     {
          return mouseUpCoords;
     }

     public double[] getDelMouseXY()
     {
          return delMouseXY;
     }

     public double[][] getBondVectors()
     {
          return bondVectors;
     }

     public int getNameSelected()
     {
          return nameSelected;
     }

     public int getSelectedName()
     {
          return selectedName;
     }

     public int getAtomNumbr()
     {
          return atomnumbr;
     }



public boolean getGlobalAxisRotationStatus()
{
     return globalAxisRotation;
}


public double[] getGlobalRotationAxes()
{
     return globalRotationAxes;
}

public String getKeyID()
{
     return keyID;
}

public String getRotationAxisID()
{
     return rotationAxisID;
}




     public int getRotType()
     {
          return rotType;
     }

     public int getRotOriginNdx()
     {
          return rotOriginNdx;
     }

     public int getAttributeModified()
     {
          return attributeModified;
     }

     public int getMouseButtonPressed()
     {
          return mouseButtonPressed;
     }

     public int getMouseButtonClicked()
     {
          return mouseButtonClicked;
     }

     public String getElemFieldText()
     {
          return elemText;
     }

     public String getSelectedElem()
     {
          return selectedElem;
     }

     public boolean getCompatBndOrdr()
     {
          return compatBndOrdr;
     }

     public boolean getelemFieldStatus()
     {
          return writeBondLength;
     }

     public boolean getShiftKeyStatus()
     {
          return shiftKeyDown;
     }

     public boolean getCntrlKeyStatus()
     {
          return cntrlKeyDown;
     }     

     public boolean getAltKeyStatus()
     {
          return altKeyDown;
     }     

     public boolean getDeleteKeyStatus()
     {
          return deleteKeyDown;
     }     

     public boolean getBondVectorSelectionStatus()
     {
          return bondVectorSelected;
     }

     public boolean getAddBondState()
     {
          return addBond;
     }

     public boolean getAddElementState()
     {
          return addElement;
     }

     public boolean getAddFragmentState()
     {
          return addFragment;
     }

     public boolean getAddFcnalGrpState()
     {
          return addFcnalGrp;
     }

     public boolean getTranslateAtomsState()
     {
          return translateAtoms;
     }

     public boolean getMouseDraggedState()
     {
          return draggedMouse;
     }

     public boolean getMouseReleasedState()
     {
          return releasedMouse;
     }

     public boolean getXYZKeyDown()
     {
          return xyzKeyDown;
     }

     public boolean getFragmentSelectedStatus()
     {
          return fragmentSelected;
     }

     public boolean getBondAngleSelectedStatus()
     {
          return bondAngleSelected;
     }

     public boolean getEditMode()
     {
          return editMode;
     }

     public ArrayList<Integer> getXYZKeyStatus()
     {
          return xyzKeys;
     }

     public double getBeta()
     {
          return beta;
     }

     public ArrayList<Integer> getSelectedNames()
     {
          return selectedNames;
     }

     public ArrayList<Integer> getSelectedComponents()
     {
          return selectedComponents;
     }

     public ArrayList<Integer> getSelectedFragmentAtoms()
     {
          return selectedFragmentAtoms;
     }

     public ArrayList<Integer> getTranslatedAtomIndices()
     {
          return translatedAtomIndices;
     }

     public void setStructureMenuEnabledStatus(boolean status)
     {
          for (ndx = 0; ndx < structureMenuItems.size(); ndx++)
          {
               structureMenuItems.get(ndx).setEnabled(status);
          }
     }

     public ArrayList<Integer> getBondedAtomsList(double[][] connectivity, int nnNdx)
     {
          int alNdx;            // bonded atoms ArrayList index
          int listNdx;          // index of atom selected from list of bonded atoms (bondedAtomsList) 
          int baListSize;       // length of bondedAtomsList

          ArrayList<Integer> bondedAtomsList = new ArrayList<Integer>();

          // Add atom bonded to H-donor to bondedAtomsList
          bondedAtomsList.add(nnNdx);

          alNdx = 0;

          baListSize = bondedAtomsList.size();         

          while (baListSize > alNdx)
          {
               alNdx++;

               listNdx = bondedAtomsList.get(alNdx);

               for (k = 0; k < natoms; k++)
               {
                    if (connectivity[listNdx][k] > 0) 
                    {
                        if (!bondedAtomsList.contains(k)) 
                        {
                             bondedAtomsList.add(k);
                        } 
                    }
               }

               baListSize = bondedAtomsList.size();    
          }

          return bondedAtomsList;
     }

     public boolean getClearDisplayStatus()
     {
          return eraseMolecule;
     }


public void setDHAtmTypTxtArea(ArrayList<String> dhAtomTypeList)
{
     for (k = 0; k < dhAtomTypeList.size(); k++)
     {
         strDHAtmTypTxtArea += dhAtomTypeList.get(k) + '\n';
     }
}     

public String getDHAtmTypTxtArea()
{
     return strDHAtmTypTxtArea;
}



     public void clearDisplay()
     {

/*
          natoms = 0;  nbondVectors = 0;
          theta = phi = psi = 0.0;          
          alpha = beta = omega = 0.0;
          epsilon = eta = 0.0;

          atomCoordinates = new double[natoms][0];
          atomConnectivity = new int[natoms][0];
          bondVectors = new double[nbondVectors][0];

          molScaleFactor = canvasScaleFactor = combScaleFactor = 1.0;

          selectMode = 1;
          glcanvas.repaint();
*/

          int response = 2;

          File tmpFiles;
          File[] flist;

          // If not undoing or redoing a user defined structure, ask if user wants to save current structure to file 
//          if ( (!undo) && (!redo) )
//          if ( (!undo) && (!redo) && (!mouseWasDragged) ) 
          if ( (!undo) && (!redo) )
          {
//               File udsFile = new File(strUserDefStructureDir + "\\" + moleculeName + ".mol2");
               File udsFile = new File(strUserDefStructureDir + fsep + moleculeName + ".mol2");


               int molNameLength = moleculeName.length();

               File tmpUdsFile = new File(strTmpUserDefStructureDir + moleculeName + ".mol2");

               Object[] selectionOptions = { "Yes", "No", "Cancel" };
 
               response = JOptionPane.showOptionDialog(glcanvas, "          Do you want to save the current structure as a User-Defined Molecule?", "Save current file", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, selectionOptions, selectionOptions[1]);

               if (response == 0)                           // If response is 'Yes', do not delete current file
               {
                    // Check for overwriting saved file
               }
               else if (response == 1)                      // If response is 'No', delete current file
               {
                    // Delete user defined structure file
                    if (udsFile.exists())
                    {
                         udsFile.delete();
                    } 

                    // Delete all temporary user defined structure files
                    tmpFiles = new File(strTmpUserDefStructureDir);

                    flist = tmpFiles.listFiles();

                    for (k = 0; k < flist.length; k++)
                    {
                         flist[k].delete();                    } 
               }
          }

          if ( (response == 0) || (response == 1) )
          {
               natoms = 0;  nbondVectors = 0;
               theta = phi = psi = 0.0;          
               alpha = beta = omega = 0.0;
               epsilon = eta = 0.0;

               mux = muy = muz = 0.0;

               atomCoordinates = new double[natoms][0];
               atomConnectivity = new int[natoms][0];
               bondVectors = new double[nbondVectors][0];

               molScaleFactor = canvasScaleFactor = combScaleFactor = 1.0;

               // Disable 'Draw Atom', 'Draw Functional Group', and 'Draw Fragment' items in Structure menu
               structureMenuItems.get(0).setEnabled(false);
               structureMenuItems.get(1).setEnabled(false);
               structureMenuItems.get(2).setEnabled(false);

               displayAtomTypeTableItem.setEnabled(false);
               displayBondParametersTableItem.setEnabled(false);
               displayDihedralParametersTableItem.setEnabled(false);
          }

//ndihedrals = 0;
minDHCentralBondIndices.clear();  maxDHCentralBondIndices.clear();

          displayAtomTypeTableItem.setEnabled(false);
          displayBondParametersTableItem.setEnabled(false);
          displayDihedralParametersTableItem.setEnabled(false);


//while(dhParametersTable.getRowCount() > 0)
//{
//     dhParametersTable.remove(0);
//}

//dhParametersTable.setVisible(false);



          selectMode = 1;
          glcanvas.repaint();
     }

     public void clearDataFields()
     {     
          elemField.setText(" "); atomTypeField.setText(" "); 
          bondField.setText(" "); bondLengthField.setText(" ");
          xCoordField.setText(" "); yCoordField.setText(" "); zCoordField.setText(" "); 
          bondAngleField.setText(" ");  dihedralAngleField.setText(" ");

          elemLabel.setVisible(false);          elemField.setVisible(false);
          atomTypeLabel.setVisible(false);      atomTypeField.setVisible(false);
          chargeLabel.setVisible(false);        chargeField.setVisible(false);

          bondLabel.setVisible(false);          bondField.setVisible(false); 
          bondLengthLabel.setVisible(false);    bondLengthField.setVisible(false);
          xLabel.setVisible(false);             xCoordField.setVisible(false);
          yLabel.setVisible(false);             yCoordField.setVisible(false);
          zLabel.setVisible(false);             zCoordField.setVisible(false);                    
          bondAngleLabel.setVisible(false);     bondAngleField.setVisible(false);
          dihedralBondLabel.setVisible(false);  dihedralBondField.setVisible(false); 
          dihedralAngleLabel.setVisible(false); dihedralAngleField.setVisible(false);
     }

     public void writeSelectedAttributes(ArrayList<Integer> selectedComponents, int textFlag)
     {
          int selectedIndex, pointNdx, ndecimals, ndx1, ndx2, ndx3, ndx4, indx1, indx2, bndOrder;
          double K, expn, f, dx, dy, dz;

          String X, Y, Z, str, Qtext, elem1, elem2, sep, bondAngle, dihedralBondFieldTxt, dihedralAtomTypeTxt;

          expn = 5;
          f = Math.pow(10, expn);

          str = "";

          switch (textFlag)
          {
               case 1:  selectedIndex = selectedComponents.get(0);

                        elemLabel.setVisible(true);  
                        elemField.setVisible(true);
                        elemField.setText(atomNames.get(selectedIndex));

                        atomTypeLabel.setVisible(true);  atomTypeField.setVisible(true);
                        chargeLabel.setVisible(true);    chargeField.setVisible(true);

                        nsubstructures = getNsubstructures();
                        atomCharges = getAtomCharges();

                        if ( (atomTypes.size() > 0) && (nsubstructures == 1) )
                        {
                             atomTypeField.setText(atomTypes.get(selectedIndex));

                             Qtext = Double.toString(atomCharges.get(selectedIndex));
                             chargeField.setText(Qtext);
                        }
                        else
                        {
                             atomTypeField.setText("N/A");
                             chargeField.setText("N/A");
                        }

                        K = f * atomCoordinates[selectedIndex][1];
                        K = Math.round(K);
                        K = K/f;
                        X = Double.toString(K);

                        pointNdx = X.indexOf(".");
                        ndecimals = (X.length() - (pointNdx + 1));

                        if (ndecimals == 1)
                        {
                             str = "0000";
                        }
                        else if (ndecimals == 2) 
                        {
                             str = "000";
                        }                       
                        else if (ndecimals == 3)
                        {
                             str = "00";
                        }
                        else if (ndecimals == 4) 
                        {
                             str = "0";
                        }                       

                        X = X.concat(str);

                        K = f * atomCoordinates[selectedIndex][2];
                        K = Math.round(K);
                        K = K/f;
                        Y = Double.toString(K);

                        pointNdx = Y.indexOf(".");
                        ndecimals = (Y.length() - (pointNdx + 1));

                        if (ndecimals == 1)
                        {
                             str = "0000";
                        }
                        else if (ndecimals == 2) 
                        {
                             str = "000";
                        }                       
                        else if (ndecimals == 3)
                        {
                             str = "00";
                        }
                        else if (ndecimals == 4) 
                        {
                             str = "0";
                        }                       

                        Y = Y.concat(str);

                        K = f * atomCoordinates[selectedIndex][3];
                        K = Math.round(K);
                        K = K/f;
                        Z = Double.toString(K);

                        pointNdx = Z.indexOf(".");
                        ndecimals = (Z.length() - (pointNdx + 1));

                        if (ndecimals == 1)
                        {
                             str = "0000";
                        }
                        else if (ndecimals == 2) 
                        {
                             str = "000";
                        }                       
                        else if (ndecimals == 3)
                        {
                             str = "00";
                        }
                        else if (ndecimals == 4) 
                        {
                             str = "0";
                        }                       

                        Z = Z.concat(str);
     
                        // Write uncentered coordinates to coordinate text fields
                        xLabel.setVisible(true); 
                        xCoordField.setVisible(true);
                        xCoordField.setText(X);

                        yLabel.setVisible(true); 
                        yCoordField.setVisible(true);
                        yCoordField.setText(Y);

                        zLabel.setVisible(true); 
                        zCoordField.setVisible(true);
                        zCoordField.setText(Z);

                        break;

               case 2:  selectedIndex = selectedComponents.get(0);

                        bondLabel.setVisible(true); 
                        bondField.setVisible(true);  

                        bondLengthLabel.setVisible(true); 
                        bondLengthField.setVisible(true);
      
                        sep = new String();

                        atomConnectivity = getAtomConnectivity();
                            
                        ndx1 = (selectedIndex/1000000) - 1;  elem1 = elemID.get(ndx1);  
                        ndx2 = selectedIndex % 1000000;      elem2 = elemID.get(ndx2);

                        // If assigning parameter values for a force field, add data to parameterFrame
                        if (paramFrameVisible)
                        {
                             // Clear parameter values when new bond is selected
                             tfKb.setText("");  tfR0.setText("");

                             alAtomNames.clear();  alAtomTypes.clear();

                             if (!albondID.contains(selectedIndex))
                             {
                                  albondID.add(selectedIndex);
                             
                                  alAtomNames.add(atomNames.get(ndx1));  alAtomNames.add(atomNames.get(ndx2));

                                  String strSelectedBonds = "     " + atomNames.get(ndx1) + " - " + atomNames.get(ndx2);

                                  // Add selected bond to jcombobox
                                  cmbSelectedBonds.addItem(strSelectedBonds);

                                  // Display selected bond to jcombobox text field
                                  cmbSelectedBonds.setSelectedItem(strSelectedBonds);
                        
                                  alAtomTypes.add(atomTypes.get(ndx1));  alAtomTypes.add(atomTypes.get(ndx2));

                                  tfAtomTypes.setText(alAtomTypes.get(0) + " - " + alAtomTypes.get(1));
                             }
                             else
                             {
                                  // Delete all text from jcombobox and text field if bond has already been selected
                                  cmbSelectedBonds.setSelectedItem(null);  tfAtomTypes.setText("");
                             }
                        }

                        bndOrder = (int)atomConnectivity[ndx1][ndx2];

                        if (bndOrder == 1) 
                        {
                             sep = " - ";
                        }
                        else if (bndOrder == 2)
                        {
                             sep = " = ";
                        }
                        else if (bndOrder == 3)
                        {
                             sep = "  ";
                        }

                        bondField.setText(atomNames.get(ndx1) + sep + atomNames.get(ndx2));

                        atomCoordinates = getCoordinates();

                        dx = atomCoordinates[ndx2][1] - atomCoordinates[ndx1][1];
                        dy = atomCoordinates[ndx2][2] - atomCoordinates[ndx1][2];
                        dz = atomCoordinates[ndx2][3] - atomCoordinates[ndx1][3];

                        elemText = Double.toString(Math.sqrt(dx*dx + dy*dy + dz*dz));
                        pointNdx = elemText.indexOf(".");
                        strlngth = Math.min( (elemText.length() - pointNdx), pointNdx+5); 
                        bondLengthField.setText(elemText.substring(0, strlngth));

                        break;

              case 3:   bondLabel.setVisible(true); 
                        bondField.setVisible(true);  

                        bondAngleLabel.setVisible(true);  
                        bondAngleField.setVisible(true);

                        sep = " - ";

                        ndx1 = selectedComponents.get(0);  ndx2 = selectedComponents.get(1);  ndx3 = selectedComponents.get(2);  

                        bondField.setText(atomNames.get(ndx1) + sep + atomNames.get(ndx2) + sep + atomNames.get(ndx3));

                        computeBondAngle(selectedComponents.get(1), selectedComponents);

                        bondAngleField.setText(getStrBondAngle());

                        break;


              case 4:   if (!bvDihedralSelected)
                        {
                             dihedralBondLabel.setVisible(true); 
                             dihedralBondField.setVisible(true);  
                        }

                        dihedralAngleLabel.setVisible(true);  
                        dihedralAngleField.setVisible(true);

                        sep = " - ";

                        if (!bvDihedralSelected)
                        { 
                             ndx1 = selectedComponents.get(0);  ndx2 = selectedComponents.get(1);  
                             ndx3 = selectedComponents.get(2);  ndx4 = selectedComponents.get(3); 


                             dihedralBondFieldTxt = atomNames.get(ndx1) + sep + atomNames.get(ndx2) + sep + atomNames.get(ndx3) + sep + atomNames.get(ndx4);

                             dihedralBondField.setText(dihedralBondFieldTxt);

                             if (dhOptFrame.isVisible())
                             {
                                  dihedralAtomTypeTxt = atomTypes.get(ndx1) + sep + atomTypes.get(ndx2) + sep + atomTypes.get(ndx3) + sep + atomTypes.get(ndx4); 

                                  dhAtomsTxt.setText(dihedralBondFieldTxt);
 
                                  dhAtmTypTxt.setText(dihedralAtomTypeTxt);

                                  dhSelection1Rb.setEnabled(true);  dhSelection2Rb.setEnabled(true);
                             }
                        }
                             computeDihedralAngle(selectedComponents);

                        dihedralAngleField.setText(getStrDihedralAngle());

                        break;
          }
     }

     public void deleteSelectedAttributes()
     {
          bondLabel.setVisible(false); 
          bondField.setText("");  bondField.setVisible(false);  
          bondLengthLabel.setVisible(false); 
          bondLengthField.setText("");  bondLengthField.setVisible(false);
          bondAngleLabel.setVisible(false);  
          bondAngleField.setText("");  bondAngleField.setVisible(false);
          dihedralBondLabel.setVisible(false);  
          dihedralBondField.setText("");  dihedralBondField.setVisible(false);          
          dihedralAngleLabel.setVisible(false);  
          dihedralAngleField.setText("");  dihedralAngleField.setVisible(false);
          atomTypeLabel.setVisible(false);  
          atomTypeField.setText("");  atomTypeField.setVisible(false);
          chargeLabel.setVisible(false);  
          chargeField.setText("");  chargeField.setVisible(false);
          xLabel.setVisible(false); 
          xCoordField.setText("");  xCoordField.setVisible(false);
          yLabel.setVisible(false); 
          yCoordField.setText("");  yCoordField.setVisible(false);
          zLabel.setVisible(false); 
          zCoordField.setText("");  zCoordField.setVisible(false);

          elemLabel.setVisible(false);  
          elemField.setText("");  elemField.setVisible(false);
     }

     public void writeTextFields(ArrayList<Integer> selectedNames)
     {
          int name, pointNdx, strlngth, ndx1, ndx2, bndOrder;
          String X, Y, Z, elem1, elem2, sep;

          if (selectedNames.size() == 1)
          {
               name = selectedNames.get(0);          
               natoms = getNatoms();
               elemID = getElemID();

               if ( (name >= 0) && (name < natoms) )      
               {
                    cntrlKeyDown = getCntrlKeyStatus();

                    if (!cntrlKeyDown)
                    {
                         clearDataFields();

                         elemLabel.setVisible(true);      elemField.setVisible(true);
                         atomTypeLabel.setVisible(true);  atomTypeField.setVisible(true);
                         chargeLabel.setVisible(true);    chargeField.setVisible(true);
                         xLabel.setVisible(true);         xCoordField.setVisible(true);
                         yLabel.setVisible(true);         yCoordField.setVisible(true);
                         zLabel.setVisible(true);         zCoordField.setVisible(true);

                         X = Double.toString(atomCoordinates[name][1] + delCoords[0]);

                         pointNdx = X.indexOf(".");
                         strlngth = Math.min( (X.length() - pointNdx), pointNdx+6);
                         X = X.substring(0, strlngth);

                         Y = Double.toString(atomCoordinates[name][2] + delCoords[1]);
                         pointNdx = Y.indexOf(".");
                         strlngth = Math.min( (Y.length() - pointNdx), pointNdx+6);
                         Y = Y.substring(0, strlngth);

                         Z = Double.toString(atomCoordinates[name][3] + delCoords[2]);
                         pointNdx = Z.indexOf(".");
                         strlngth = Math.min( (Z.length() - pointNdx), pointNdx+6);
                         Z = Z.substring(0, strlngth);
     
                         // Write uncentered coordinates to coordinate text fields
                         elemField.setText(atomNames.get(name));
                         xCoordField.setText(X);
                         yCoordField.setText(Y);
                         zCoordField.setText(Z);

                         atomTypeField.setText(atomTypes.get(name));
                    }
               }
               else if (name >= 1000000)
               {
                    double dx, dy, dz;
                    sep = new String();

                    clearDataFields();

                    bondLabel.setVisible(true); bondField.setVisible(true); 
                    bondLengthLabel.setVisible(true);  bondLengthField.setVisible(true);

                    atomConnectivity = getAtomConnectivity();
                            
                    ndx1 = (name/1000000) - 1;  elem1 = elemID.get(ndx1);  
                    ndx2 = name % 1000000;      elem2 = elemID.get(ndx2);

                    bndOrder = (int)atomConnectivity[ndx1][ndx2];

                    if (bndOrder == 1) 
                    {
                       sep = " - ";
                    }
                    else if (bndOrder == 2)
                    {
                       sep = " = ";
                    }
                    else if (bndOrder == 3)
                    {
                       sep = "  ";
                    }

                    bondField.setText(atomNames.get(ndx1) + sep + atomNames.get(ndx2));

                    atomCoordinates = getCoordinates();

                    dx = atomCoordinates[ndx2][1] - atomCoordinates[ndx1][1];
                    dy = atomCoordinates[ndx2][2] - atomCoordinates[ndx1][2];
                    dz = atomCoordinates[ndx2][3] - atomCoordinates[ndx1][3];

                    elemText = Double.toString( Math.sqrt(dx*dx + dy*dy + dz*dz));
                    pointNdx = strval.indexOf(".");
                    strlngth = Math.min( (elemText.length() - pointNdx), pointNdx+5); 
                    bondLengthField.setText(elemText.substring(0, strlngth));
               }
               else if (name <= (-100))
               {
                    // No text field display for bond vectors

                    clearDataFields();
               }
          }
          else if (selectedNames.size() == 3)
          {
               bondAngleField.setText(getStrBondAngle());

               dihedralAngleField.setText(getStrDihedralAngle());             
          }
     }

     public double[][] resizeDoubleMatrix(int ncurrentRows, int naddedRows, int ncols, double[][]currentArray)
     {
          double[][] tmpArray = new double[ncurrentRows][ncols];

          for (j = 0; j < ncurrentRows; j++)
          {
               for (k = 0; k < ncols; k++)
               { 
                    tmpArray[j][k] = currentArray[j][k];
               }
          }
 
          ncurrentRows += naddedRows;

          currentArray = new double[ncurrentRows][ncols];

          for (j = 0; j < (ncurrentRows - naddedRows); j++)
          {
               for (k = 0; k < ncols; k++)
               {
                    currentArray[j][k] = tmpArray[j][k];
               }
          }     

          return currentArray;
     }

 


//public double[] orientGlobalAxes(double[] globalRotationAxes, double theta, double phi, double psi)
//public double[] orientGlobalAxes(String rotAxis, double theta, double phi, double psi)
public void orientGlobalAxes(String rotAxis, double theta, double phi, double psi)
{
//     double nxprime, nyprime, nzprime;

//     keyID = getKeyID();

     double n1star, n2star, n3star;



     if (rotAxis.equals("x"))
     {

//       deltaAngle = Math.abs(deltaAngle);

          nxx1 = 1.0;  nxx2 = nxx3 = 0.0;

if (deltaAngle > 0)
{
   delPsi = -delPsi;
}


          n2star = nyy2*Math.cos( (Math.PI/180)*deltaAngle ) + nyy3*Math.sin( (Math.PI/180)*deltaAngle );
          n3star = -nyy2*Math.sin( (Math.PI/180)*deltaAngle ) + nyy3*Math.cos( (Math.PI/180)*deltaAngle );

          nyy2 = n2star;  nyy3 = n3star;

          n2star = nzz2*Math.cos( (Math.PI/180)*deltaAngle ) + nzz3*Math.sin( (Math.PI/180)*deltaAngle );
          n3star = -nzz2*Math.sin( (Math.PI/180)*deltaAngle ) + nzz3*Math.cos( (Math.PI/180)*deltaAngle );

          nzz2 = n2star;  nzz3 = n3star;
     }





/*

     rotationAxisID = getRotationAxisID();

nxx = globalRotationAxes[0];  nyy = globalRotationAxes[1];  nzz = globalRotationAxes[2];


//     if (keyID.equals("x"))
     if (rotationAxisID.equals("x"))
     {
          // Rotate y- and z-axes about x-axis by an angle '(psi - psi0)'

          globalRotationAxes[0] = 1.0;
          globalRotationAxes[1] = nyy*(Math.cos((Math.PI/180)*(psi - psi0))) - nzz*(Math.sin((Math.PI/180)*(psi - psi0)));
          globalRotationAxes[2] = nyy*(Math.sin((Math.PI/180)*(psi - psi0))) + nzz*(Math.cos((Math.PI/180)*(psi - psi0)));

//          globalRotationAxes[0] = 1.0;
//          globalRotationAxes[1] = (Math.cos((Math.PI/180)*psi)) - (Math.sin((Math.PI/180)*psi));
//          globalRotationAxes[2] = (Math.sin((Math.PI/180)*psi)) + (Math.cos((Math.PI/180)*psi));

//          globalRotationAxes[1] = (Math.cos((Math.PI/180)*(psi - psi0))) - (Math.sin((Math.PI/180)*(psi - psi0)));
//          globalRotationAxes[2] = (Math.sin((Math.PI/180)*(psi - psi0))) + (Math.cos((Math.PI/180)*(psi - psi0)));


     }
//     else if (keyID.equals("y"))
     else if (rotationAxisID.equals("y"))
     {
          // Rotate x- and z-axes about y-axis by an angle 'theta'

          globalRotationAxes[0] = nxx*(Math.cos((Math.PI/180)*(theta - theta0))) - nzz*(Math.sin((Math.PI/180)*(theta - theta0)));
          globalRotationAxes[1] = 1.0;
          globalRotationAxes[2] = nxx*(Math.sin((Math.PI/180)*(theta - theta0))) + nzz*(Math.cos((Math.PI/180)*(theta - theta0)));

//          globalRotationAxes[0] = (Math.cos((Math.PI/180)*theta)) - (Math.sin((Math.PI/180)*theta));
//          globalRotationAxes[1] = 1.0;
//          globalRotationAxes[2] = (Math.sin((Math.PI/180)*theta)) + (Math.cos((Math.PI/180)*theta));


     }
//     else if (keyID.equals("z"))
     else if (rotationAxisID.equals("z"))
     {
          // Rotate x- and y-axes about z-axis by an angle 'phi'

          globalRotationAxes[0] = nxx*(Math.cos((Math.PI/180)*(phi - phi0))) - nyy*(Math.sin((Math.PI/180)*(phi - phi0)));
          globalRotationAxes[1] = nxx*(Math.sin((Math.PI/180)*(phi - phi0))) + nyy*(Math.cos((Math.PI/180)*(phi - phi0)));
          globalRotationAxes[2] = 1.0;

//          globalRotationAxes[0] = (Math.cos((Math.PI/180)*phi)) - (Math.sin((Math.PI/180)*phi));
//          globalRotationAxes[1] = (Math.sin((Math.PI/180)*phi)) + (Math.cos((Math.PI/180)*phi));
//          globalRotationAxes[2] = 1.0;

     }

*/

      rotationAxisID = "0";


//     return globalRotationAxes;

}


    public void setElementRGB(ArrayList<Integer> AN, int natoms)
     {
          float r, g, b;
          r = 0.0f; g = 0.0f; b = 0.0f;

          RGB = new float[natoms][ncolors];
        
          for (j = 0; j < natoms; j++)
          {
               switch(AN.get(j))
               {
                    case 1: r = 1.0000f; g = 1.0000f; b = 1.0000f;   // H
                            break;
                    case 2: r = 0.8510f; g = 1.0000f; b = 1.0000f;   // He
                            break;
                    case 3: r = 0.7529f; g = 0.7529f; b = 0.7529f;   // Li
                            break;
                    case 4: r = 0.7725f; g = 1.0000f; b = 0.0000f;   // Be
                            break;
                    case 5: r = 1.0000f; g = 0.7176f; b = 0.7176f;   // B
                            break; 
                    case 6: r = 0.5098f; g = 0.5098f; b = 0.5098f;   // C
                            break;
                    case 7: r = 0.0000f; g = 0.0000f; b = 1.0000f;   // N
                            break;
                    case 8: r = 1.0000f; g = 0.0000f; b = 0.0000f;   // O
                            break;
                    case 9: r = 0.7020f; g = 1.0000f; b = 1.0000f;   // F
                            break;
                   case 10: r = 0.6863f; g = 0.8902f; b = 0.9569f;   // Ne
                            break;
                   case 11: r = 0.6667f; g = 0.3686f; b = 0.9490f;   // Na
                            break;
                   case 12: r = 0.5373f; g = 1.0000f; b = 0.0000f;   // Mg
                            break; 
                   case 13: r = 1.0000f; g = 0.3922f; b = 1.0000f;   // Al
                            break;
                   case 14: r = 1.0000f; g = 0.7843f; b = 0.1569f;   // Si
                            break;
                   case 15: r = 0.9137f; g = 0.6118f; b = 1.0000f;   // P
                            break;
                   case 16: r = 1.0000f; g = 0.9216f; b = 0.0000f;   // S
                            break;
                   case 17: r = 0.7020f; g = 1.0000f; b = 0.6784f;   // Cl
                            break;
                   case 18: r = 0.5059f; g = 0.8196f; b = 0.8941f;   // Ar
                            break;
                   case 19: r = 0.5608f; g = 0.2549f; b = 0.8275f;   // K
                            break;
                   case 20: r = 0.2392f; g = 1.0000f; b = 0.0000f;   // Ca
                            break;
                   case 21: r = 0.9020f; g = 0.9020f; b = 0.8941f;   // Sc
                            break;
                   case 22: r = 0.7529f; g = 0.7647f; b = 0.7765f;   // Ti
                            break;
                   case 23: r = 0.6549f; g = 0.6471f; b = 0.6745f;   // V
                            break;
                   case 24: r = 0.5451f; g = 0.6000f; b = 0.7765f;   // Cr
                            break;
                   case 25: r = 0.6118f; g = 0.4824f; b = 0.7765f;   // Mn
                            break;
                   case 26: r = 0.5059f; g = 0.4824f; b = 0.7765f;   // Fe
                            break;
                   case 27: r = 0.3647f; g = 0.4275f; b = 1.0000f;   // Co
                            break;
                   case 28: r = 0.3647f; g = 0.4824f; b = 0.7647f;   // Ni
                            break;
                   case 29: r = 1.0000f; g = 0.4824f; b = 0.3843f;   // Cu
                            break;
                   case 30: r = 0.4863f; g = 0.5059f; b = 0.6863f;   // Zn
                            break;
                   case 31: r = 0.7647f; g = 0.5725f; b = 0.5686f;   // Ga
                            break;
                   case 32: r = 0.4000f; g = 0.6784f; b = 0.3832f;   // Ge
                            break;
                   case 33: r = 0.7451f; g = 0.5059f; b = 0.8902f;   // As
                            break;
                   case 34: r = 1.0000f; g = 0.6353f; b = 0.0000f;   // Se
                            break;
                   case 35: r = 0.8275f; g = 0.3098f; b = 0.2706f;   // Br
                            break;
                   case 36: r = 0.3647f; g = 0.7294f; b = 0.8196f;   // Kr
                            break;
                   case 37: r = 0.4431f; g = 0.1804f; b = 0.6980f;   // Rb
                            break;
                   case 38: r = 0.0000f; g = 1.0000f; b = 0.0000f;   // Sr
                            break;
                   case 39: r = 0.5882f; g = 0.9922f; b = 1.000f;    // Y
                            break;
                   case 40: r = 0.5882f; g = 0.8824f; b = 0.8824f;   // Zr
                            break;
                   case 41: r = 0.4549f; g = 0.7647f; b = 0.7961f;   // Nb
                            break;
                   case 42: r = 0.3333f; g = 0.7098f; b = 0.7176f;   // Mo
                            break;
                   case 43: r = 0.2353f; g = 0.6235f; b = 0.6588f;   // Tc
                            break; 
                   case 44: r = 0.1373f; g = 0.5569f; b = 0.5922f;   // Ru
                            break;
                   case 45: r = 0.0431f; g = 0.4863f; b = 0.5490f;   // Rh
                            break;
                   case 46: r = 0.0000f; g = 0.4118f; b = 0.5255f;   // Pd
                            break;
                   case 47: r = 0.6000f; g = 0.7765f; b = 1.0000f;   // Ag
                            break;
                   case 48: r = 1.0000f; g = 0.8471f; b = 0.5686f;   // Cd
                            break;
                   case 49: r = 0.6549f; g = 0.4627f; b = 0.4510f;   // In
                            break;
                   case 50: r = 0.4000f; g = 0.5059f; b = 0.5059f;   // Sn
                            break;
                   case 51: r = 0.6235f; g = 0.3961f; b = 0.7098f;   // Sb
                            break;
                   case 52: r = 0.8353f; g = 0.4824f; b = 0.0000f;   // Te
                            break;
                   case 53: r = 0.6667f; g = 0.3098f; b = 0.2706f;   // I
                            break;
                   case 54: r = 0.2588f; g = 0.6235f; b = 0.6902f;   // Xe
                            break;
                   case 55: r = 0.3412f; g = 0.0980f; b = 0.5608f;   // Cs
                            break; 
                   case 56: r = 0.0000f; g = 0.7961f; b = 0.0000f;   // Ba
                            break;
                   case 57: r = 0.4392f; g = 0.8706f; b = 1.0000f;   // La
                            break;
                   case 58: r = 1.0000f; g = 1.0000f; b = 0.7843f;   // Ce
                            break;
                   case 59: r = 0.8510f; g = 1.0000f; b = 0.7843f;   // Pr
                            break;
                   case 60: r = 0.7412f; g = 1.0000f; b = 0.7843f;   // Nd
                            break;
                   case 61: r = 0.6431f; g = 1.0000f; b = 0.7843f;   // Pm
                            break;
                   case 62: r = 0.5725f; g = 1.0000f; b = 0.7843f;   // Sm
                            break;
                   case 63: r = 0.3882f; g = 1.0000f; b = 0.7843f;   // Eu
                            break;
                   case 64: r = 0.2784f; g = 1.0000f; b = 0.7843f;   // Gd
                            break;
                   case 65: r = 0.1961f; g = 1.0000f; b = 0.7843f;   // Tb
                            break;
                   case 66: r = 0.1216f; g = 1.0000f; b = 0.7176f;   // Dy
                            break;
                   case 67: r = 0.0000f; g = 1.0000f; b = 0.6157f;   // Ho
                            break;
                   case 68: r = 0.0000f; g = 0.9059f; b = 0.4627f;   // Er
                            break;
                   case 69: r = 0.0000f; g = 0.8275f; b = 0.3255f;   // Tm
                            break;
                   case 70: r = 0.0000f; g = 0.7529f; b = 0.2235f;   // Yb
                            break;
                   case 71: r = 0.0000f; g = 0.6784f; b = 0.1373f;   // Lu
                            break;
                   case 72: r = 0.3020f; g = 0.7608f; b = 1.0000f;   // Hf
                            break;
                   case 73: r = 0.3020f; g = 0.6549f; b = 1.0000f;   // Ta
                            break;
                   case 74: r = 0.1529f; g = 0.5804f; b = 0.8392f;   // W
                            break;
                   case 75: r = 0.1529f; g = 0.4941f; b = 0.6745f;   // Re
                            break;
                   case 76: r = 0.1529f; g = 0.4078f; b = 0.5922f;   // Os
                            break;
                   case 77: r = 0.0941f; g = 0.3333f; b = 0.5294f;   // Ir
                            break;
                   case 78: r = 0.0941f; g = 0.3569f; b = 0.5686f;   // Pt
                            break;
                   case 79: r = 1.0000f; g = 0.8196f; b = 0.1412f;   // Au
                            break;
                   case 80: r = 0.7098f; g = 0.7098f; b = 0.7647f;   // Hg
                            break;
                   case 81: r = 0.6549f; g = 0.3333f; b = 0.3020f;   // Tl
                            break;
                   case 82: r = 0.3412f; g = 0.3529f; b = 0.3765f;   // Pb
                            break;
                   case 83: r = 0.6235f; g = 0.3098f; b = 0.7098f;   // Bi
                            break;
                   case 84: r = 0.6745f; g = 0.3647f; b = 0.0000f;   // Po
                            break;
                   case 85: r = 0.4627f; g = 0.3098f; b = 0.2706f;   // At
                            break;
                   case 86: r = 0.2588f; g = 0.5176f; b = 0.5922f;   // Rn
                            break;
                   case 87: r = 0.2588f; g = 0.0000f; b = 0.4000f;   // Fr
                            break;
                   case 88: r = 0.0000f; g = 0.4863f; b = 0.0000f;   // Ra
                            break;
                   case 89: r = 0.4431f; g = 0.6667f; b = 0.9882f;   // Ac
                            break;
                   case 90: r = 0.0000f; g = 0.7333f; b = 1.0000f;   // Th
                            break;
                   case 91: r = 0.0000f; g = 0.6314f; b = 1.0000f;   // Pa
                            break;
                   case 92: r = 0.0000f; g = 0.5725f; b = 1.0000f;   // U
                            break;
                   case 93: r = 0.0000f; g = 0.5059f; b = 0.9490f;   // Np
                            break;
                   case 94: r = 0.0000f; g = 0.4196f; b = 0.9490f;   // Pu
                            break;
                   case 95: r = 0.3333f; g = 0.3569f; b = 0.9490f;   // Am
                            break;
                   case 96: r = 0.4706f; g = 0.3569f; b = 0.8902f;   // Cm
                            break;
                   case 97: r = 0.5373f; g = 0.3098f; b = 0.8902f;   // Bk
                            break;
                   case 98: r = 0.6314f; g = 0.2157f; b = 0.8353f;   // Cf
                            break;
                   case 99: r = 0.7020f; g = 0.1216f; b = 0.8353f;   // Es
                            break;
                  case 100: r = 0.7020f; g = 0.1216f; b = 0.7294f;   // Fm
                            break;
                  case 101: r = 0.7020f; g = 0.0510f; b = 0.6549f;   // Md
                            break;
                  case 102: r = 0.7412f; g = 0.0510f; b = 0.5294f;   // No
                            break;
                  case 103: r = 0.7882f; g = 0.0000f; b = 0.4000f;   // Lr
                            break;
                  case 104: r = 0.1961f; g = 0.5098f; b = 1.0000f;   // Rf
                            break;
                  case 105: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Db
                            break;
                  case 106: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Sg
                            break;
                  case 107: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Bh
                            break;
                  case 108: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Hs
                            break;
                  case 109: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Mt
                            break;
                  case 110: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Ds
                            break;
                  case 111: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Rg
                            break;
               }

               RGB[j][0] = r;  RGB[j][1] = g;  RGB[j][2] = b;
          }    
     }

     public void displaySelection(GLDrawable gldraw, double[][] atomCoordinates, int[][] atomConnectivity, float[][] elementRGB, ArrayList<Double> atomRadii, double[][] bondVectors, double theta, double phi, int natoms)
     {
          gl = gldraw.getGL();
          glu = gldraw.getGLU();

          GLUquadric quadric = glu.gluNewQuadric();

          int connectivity, ndx, ndx3;

          double atomRad;
          double bd, r, r1, r2, eps;
          double angle, rot_x, rot_y, rot_z;                                        
          double dx12, dy12, dz12, rx12, ry12, rz12, rx13, ry13, rz13;
          double nx, ny, nz;
          double n11, n22, n33;

          double xh12, yh12, zh12, xh23, yh23, zh23;
          double dx23, dy23, dz23, d23;
          double nx1, ny1, nz1;    // components of vector normal to plane containing double bond 
          double n1, n2, n3;       // components of vector parallel to plane containing double 
                              // bond and normal to double bond
          double xdel, ydel, zdel;
          double vxstar, vystar, vzstar;

          float[] atomMat = new float[4];
          float[] bondMat = new float[4];

          bondRadius = 0.05; 
          atomRad = 0.30; // for glucose

          int offset = 0;
          int bufsize = 512;

          selectBuffer = new int[bufsize];
          IntBuffer selectBuf = IntBuffer.allocate(bufsize);

          gl.glSelectBuffer(bufsize, selectBuffer);

          gl.glRenderMode(GL.GL_SELECT);        // switch to selection mode

          gl.glInitNames();                     // create empty name stack

          // Save original projection matrix
          gl.glMatrixMode(GL.GL_PROJECTION);
          gl.glPushMatrix();
          gl.glLoadIdentity();

          // Get current viewport
          viewport = new int[4];

          for (j = 0; j < 4; j++)
          {
               viewport[j] = 0;
          }

          gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
 
          glu.gluPickMatrix((double)mouseX, (double)viewport[3] - (double)mouseY, 1.0, 1.0, viewport);

//          glu.gluPerspective(fov, aspect, near, far);
//          gl.glOrtho(5.0, (canvasWidth - 5.0), 5.0, (canvasHeight - 5.0), near, far);
//          glu.gluOrtho2D(5.0, (canvasWidth - 5.0), 5.0, (canvasHeight - 5.0));
          gl.glOrtho(-panelWidth/100.0, panelWidth/100.0, -panelHeight/100.0, panelHeight/100.0, -far, far);
//          gl.glOrtho(-panelWidth/100.0, panelWidth/100.0, -panelHeight/100.0, panelHeight/100.0, near, far);

          gl.glMatrixMode(GL.GL_MODELVIEW);         // restore model view

          atomRad = 0.30; 
          double psf;

          psf = 1.0;      

          // Draw atoms
          for (j = 0; j < natoms; j++)
          {
               gl.glPushName(j);

               gl.glPushMatrix();

               gl.glTranslated(psf * atomCoordinates[j][1], psf * atomCoordinates[j][2], psf * atomCoordinates[j][3]); 

               atomMat[0] = elementRGB[j][0];
               atomMat[1] = elementRGB[j][1];
               atomMat[2] = elementRGB[j][2];
               atomMat[3] = material_alphaValue; 

               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, atomMat);

               glut.glutSolidSphere(glu, atomRad, 100, 360);

               gl.glPopMatrix();

               gl.glPopName();
          }

          // Draw bonds
          for (j = 0; j < natoms; j++)
          {
               for (k = (j+1); k < natoms; k++)
               {
                    if (atomConnectivity[j][k] != 0)
                    {
                         gl.glPushName( (1000000*(j+1) + k) );

                         gl.glPushMatrix();

                         gl.glTranslated(psf * atomCoordinates[k][1], psf * atomCoordinates[k][2], psf * atomCoordinates[k][3]); 

                         dx12 = (atomCoordinates[j][1] - atomCoordinates[k][1]);
                         dy12 = (atomCoordinates[j][2] - atomCoordinates[k][2]);
                         dz12 = (atomCoordinates[j][3] - atomCoordinates[k][3]);

                         bd = psf * Math.sqrt(dx12*dx12 + dy12*dy12 + dz12*dz12);

                         r = Math.sqrt(dx12*dx12 + dy12*dy12);

                         if (r > 0.0)
                         {
                              n11 = -dy12/r;
                              n22 = dx12/r;
                              n33 = 0.0;

                              if (dz12 >= 0.0)
                              {
                                   angle = (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                              }
                              else     
                              {
                                   angle = 180.0 - (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                              }
                         }
                         else
                         {
                              n11 = 0.0;
                              n22 = 0.0;
                              n33 = 1.0;

                              if (dz12 >= 0.0)  
                              {
                                   angle = 0.0;
                              }
                              else     
                              {
                                   angle = 180.0;
                              }
                         }
 
                         bondsColored = false;
 
                         if (bondsColored == false)
                         {
                              gl.glRotated(angle, n11, n22, n33);
 
                              material_alphaValue = 1.0f;

                              bondMat[0] = 1.000f;
                              bondMat[1] = 1.000f;
                              bondMat[2] = 1.000f;
                              bondMat[3] = material_alphaValue; 

                              gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat); 

                              if (atomConnectivity[j][k] == 1)
                              {
                                   glu.gluCylinder(quadric, bondRadius, bondRadius, bd, 20, 20); 
                              }
                              else if (atomConnectivity[j][k] > 1)
                              {
                                   ndx3 = -1;

                                   for (ndx = 0; ndx < natoms; ndx++)
                                   {
                                        if ( (atomConnectivity[j][ndx] > 0) && (ndx != k) )
                                        {
                                             ndx3 = ndx;
                                             break;
                                        }
                                   }

                                   if (ndx3 < 0)
                                   {
                                        for (ndx = 0; ndx < natoms; ndx++)
                                        {
                                             if ( (atomConnectivity[k][ndx] > 0) && (ndx != j) )
                                             {
                                                  ndx3 = ndx;
                                                  break;
                                             }
                                             else
                                             {
                                                  ndx3 = 0;
                                             }
                                        }
                                   } 

                                   // Components of vector along double bond
                                   rx12 = (-dx12/bd); ry12 = (-dy12/bd); rz12 = (-dz12/bd); 

                                   // If first atom drawn has sp2 hybridization
                                   if (natoms == 2)
                                   {
                                        dx23 = (atomCoordinates[0][1] - atomCoordinates[1][1]);
                                        dy23 = (atomCoordinates[0][2] - atomCoordinates[1][2]);
                                        dz23 = (atomCoordinates[0][3] - atomCoordinates[1][3]);
                                   }
                                   else
                                   {
                                        // Compute distance between one double bonded atom and coplanar single bonded atom
                                        dx23 = (atomCoordinates[ndx3][1] - atomCoordinates[j][1]);
                                        dy23 = (atomCoordinates[ndx3][2] - atomCoordinates[j][2]);
                                        dz23 = (atomCoordinates[ndx3][3] - atomCoordinates[j][3]);
                                   }

                                   d23 = Math.sqrt(dx23*dx23 + dy23*dy23 + dz23*dz23);

                                   // Components of vector along coplanar atom pairs
                                   rx13 = dx23/d23; ry13 = dy23/d23; rz13 = dz23/d23; 

                                   if (natoms == 2)
                                   {
                                        rx13 = ry13 = rz13 = 0.0;

                                        if (bondVectors.length > 0)
                                        {
                                             int BVindx = ( -((int)(bondVectors[indx][0]/100) + 1) );

                                             for (indx = 0; indx < bondVectors.length; indx++)
                                             {                                                      
                                                  if ( (BVindx == 0) || (BVindx == 1) )
                                                  {
                                                       if ( (int)(bondVectors[indx][4]) == 1)
                                                       {
                                                            rx13 = bondVectors[indx][1];  ry13 = bondVectors[indx][2];  rz13 = bondVectors[indx][3];  
                                                            break;
                                                       }
                                                  }
                                             }
                                        }
                                   }

                                   // Compute unit vector normal to plane of the three coplanar atoms
                                   nx1 = (ry12*rz13 - ry13*rz12);
                                   ny1 = (rx13*rz12 - rx12*rz13);
                                   nz1 = (rx12*ry13 - rx13*ry12);

                                   if ( (nx1 == 0.0) && (ny1 == 0.0) && (nz1 == 0.0) )
                                   {
                                        n1 = 1.0;  n2 = n3 = 0.0;

                                        if (dz23 != 1.0)
                                        {
                                             n1 = dy23;  n2 = -dx23;  n3 = 0.0;
                                        }
                                   }
                                   else
                                   { 
                                        r = Math.sqrt((nx1*nx1) + (ny1*ny1) + (nz1*nz1));
 
                                        nx1 = nx1/r;  ny1 = ny1/r;  nz1 = nz1/r;

                                        // Compute unit vector parallel to plane containing double bond and normal to double bond
                                        n1 = (ny1*rz12 - nz1*ry12);
                                        n2 = (nz1*rx12 - nx1*rz12);
                                        n3 = (nx1*ry12 - ny1*rx12);
                                   }

                                   r = Math.sqrt((n1*n1) + (n2*n2) + (n3*n3));

                                   n1 = n1/r;  n2 = n2/r;  n3 = n3/r;

                                   eps = 1.50;  double brp = 0.65;

                                   if (atomConnectivity[j][k] == 3)
                                   {
                                        eps = 2.50;  brp = 0.65;
                                        bondRadius = 0.75*bondRadius;
                                   }

                                   xdel = eps*(n1*bondRadius);
                                   ydel = eps*(n2*bondRadius);
                                   zdel = eps*(n3*bondRadius); 
  
                                   gl.glTranslated(xdel, ydel, zdel);
//                                    gl.glRotated(-phi, xh12, yh12, zh12);
                                   glu.gluCylinder(quadric, bondRadius, bondRadius, bd, 20, 20); 
 
                                   gl.glTranslated(-2*xdel, -2*ydel, -2*zdel);
                                   glu.gluCylinder(quadric, bondRadius, bondRadius, bd, 20, 20); 
//                                    gl.glRotated(phi, xh12, yh12, zh12);
                                   gl.glTranslated(xdel, ydel, zdel);
                              }
                         }
                         else
                         {
                              bondMat1[0] = elementRGB[k][0];                 // atomColors[(3*k)];
                              bondMat1[1] = elementRGB[k][1];                 // atomColors[(3*k) + 1];
                              bondMat1[2] = elementRGB[k][2];                 // atomColors[(3*k) + 2];
                              bondMat1[3] = material_alphaValue;

                              bondMat2[0] = elementRGB[j][0];                 // atomColors[(3*j)];
                              bondMat2[1] = elementRGB[j][1];                 // atomColors[(3*j) + 1];
                              bondMat2[2] = elementRGB[j][2];                 // atomColors[(3*j) + 2];
                              bondMat2[3] = material_alphaValue;   

                              gl.glRotated(angle, n11, n22, n33);
                              gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat1); 
                              glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);

                              gl.glRotated(-angle, n11, n22, n33);
                              gl.glTranslated(dx12/2, dy12/2, dz12/2);
                              gl.glRotated(angle, n11, n22, n33);

                              gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat2); 
                              glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);
                         }

                         gl.glPopMatrix();

                         gl.glPopName();
                     }
               }
          }

          // Draw bond vectors
          if (bondVectors.length > 0)
          {
               nbvRows = bondVectors.length;

               double vx1, vx2, vy1,vy2, vz1, vz2;
               float red, green, blue;

               float[] bvColor = new float[4];
     
               red = 1.0f; green = 1.0f; blue = 1.0f; 

               for (ndx = 0; ndx < nbvRows; ndx++)
               {
                    atomIndex = - ( ((int)(bondVectors[ndx][0]/100)) + 1 );

                    if ((int)bondVectors[ndx][5] == 1)
                    {
                         vx1 = atomCoordinates[atomIndex][1];  vx2 = bondVectors[ndx][1] + atomCoordinates[atomIndex][1];
                         vy1 = atomCoordinates[atomIndex][2];  vy2 = bondVectors[ndx][2] + atomCoordinates[atomIndex][2];                         
                         vz1 = atomCoordinates[atomIndex][3];  vz2 = bondVectors[ndx][3] + atomCoordinates[atomIndex][3];

                         vxstar = 0.8*(vx2-vx1); vystar = 0.8*(vy2-vy1); vzstar = 0.8*(vz2-vz1);
                         vx2 = vx1 + vxstar; vy2 = vy1 + vystar; vz2 = vz1 + vzstar;
                            
                         nameSelected = getNameSelected();

                         if ( (int)bondVectors[ndx][0] == nameSelected )
                         {
                            bvColor[0] = 1.0f; bvColor[1] = 1.0f; bvColor[2] = 0.5f; bvColor[3] = 1.0f; 
                         }
                         else
                         {
                              bvColor[0] = red; bvColor[1] = green; bvColor[2] = blue; bvColor[3] = 1.0f;
                         }

                         gl.glLineWidth(2.0f);
          
                         gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bvColor);

                         gl.glPushName( (int)bondVectors[ndx][0] );

                         gl.glPushMatrix(); 
                       
                         gl.glBegin(GL.GL_LINES);
                         gl.glVertex3d(vx1, vy1, vz1);
                         gl.glVertex3d(vx2, vy2, vz2);
                         gl.glEnd();

                         double a1, a2,a3, axialx, axialy, axialz, csrho, snrho, rho;

                         a1 = vx2-vx1;  a2 = vy2-vy1;  a3 = vz2-vz1;

                         axialx = (-a2);  axialy = a1;  axialz = 0.0;

                         csrho = a3/(Math.sqrt( (a1*a1) + (a2*a2) + (a3*a3) ));
                         snrho = Math.sqrt( 1 - (csrho*csrho) );

                         angle = (180.0/Math.PI)*Math.acos(csrho);

                         gl.glTranslated(vx1, vy1, vz1);

                         gl.glRotated(angle, axialx, axialy, axialz);

                         glu.gluCylinder(quadric, 0.075, 0.075, 0.80, 20, 20); 

                         gl.glRotated(-angle, axialx, axialy, axialz);

                         gl.glTranslated(-vx1, -vy1, -vz1);

                         gl.glPopMatrix();
 
                         gl.glPopName();
                    }
               }
          }

          // Restore original projection matrix
          gl.glMatrixMode(GL.GL_PROJECTION);
          gl.glPopMatrix();
          gl.glMatrixMode(GL.GL_MODELVIEW);

          nhits = gl.glRenderMode(GL.GL_RENDER);

          selectMode = 0;

          processHits(nhits, selectBuffer);
     } 

     public void processHits(int nhits, int[] buffer)
     {
          int offset;
          int names, index, textFlag;
          float z1, z2, minz1;

          ArrayList<Integer> selectedComponents = new ArrayList<Integer>();

          offset = 0;
          nameSelected = -1;

          minz1 = 1.0f;

          if (nhits == 0)
          {
               textFlag = 0;

               if (mouseButtonClicked == 3)
               {
                    deleteSelectedAttributes();
               }

               glcanvas.repaint();
          }

          for (j = 0; j < nhits; j++)
          {
               names = buffer[offset];
               offset++;

               z1 = (float)buffer[offset] / 0x7FFFFFFF;
               offset++;

               z2 = (float)buffer[offset] / 0x7FFFFFFF;
               offset++;

               for (k = 0; k < names; k++)
               {
                   selectedName = buffer[offset];

                   if (z1 < minz1)
                   {
                        minz1 = z1;

                        nameSelected = selectedName;
                   }
             
                   offset++;
               }       
          }

          selectMode = 0;

          if ( (nameSelected >= 0) && (nameSelected < natoms) )
          {
               if (selectedNames.size() <= 1)
               {
                    if (nhits > 0)
                    {
                         textFlag = 1;  // single atom selected

                         selectedComponents.add(nameSelected);

                         deleteSelectedAttributes();
                         writeSelectedAttributes(selectedComponents, textFlag);
                    }
               }

               bondVectorSelected = false;

               if (cntrlKeyDown)
               {
                    if ( (selectedNames.size() > 0) && (selectedNames.contains(nameSelected)) )
                    {
                         // Delete selected atom from array if atom has been previously selected
                         index = selectedNames.indexOf(nameSelected);
                         selectedNames.remove(index);
                    }
                    else
                    {
                         // Add selected atom to array if not already present in the array
                         selectedNames.add(nameSelected);
                    }

                    deleteSelectedAttributes();
               }
               else if (!cntrlKeyDown)
               {
                    if ( (selectedNames.size() > 0) && (selectedNames.contains(nameSelected)) )
                    {
                         // Delete selected atom from array if atom has been previously selected
                         //   and atom is not being solvated
                         if (!addSolventH2O)
                         {
                              index = selectedNames.indexOf(nameSelected);
                              selectedNames.remove(index);
                         }
                    }
                    else
                    {
                         // If select atom that is not in the array, delete all atoms from array
                         //   and add currently selected atom to array 
                         selectedNames.clear();
                         selectedNames.add(nameSelected);
                    }

                    if (selectedNames.size() > 1)
                    {
                         deleteSelectedAttributes();
                    }
               }
          }
          else if (nameSelected >= 1000000)
          {
               if (selectedNames.size() <= 1)
               {
                    if (nhits > 0)
                    {
                         textFlag = 2;  // single bond selected

                         selectedComponents.add(nameSelected);

                         deleteSelectedAttributes();
                         writeSelectedAttributes(selectedComponents, textFlag);
                    }
               }

               bondVectorSelected = false;
   
               if (cntrlKeyDown)
               {
                    if ( (selectedNames.size() > 0) && (selectedNames.contains(nameSelected)) )
                    {
                         index = selectedNames.indexOf(nameSelected);
                         selectedNames.remove(index);
                    }
                    else
                    {
                         selectedNames.add(nameSelected);
                    }

                    deleteSelectedAttributes();
               }
               else if (!cntrlKeyDown)
               {
                    if ( (selectedNames.size() > 0) && (selectedNames.contains(nameSelected)) )
                    {
                         index = selectedNames.indexOf(nameSelected);
                         selectedNames.remove(index);
                    }
                    else
                    {
                         selectedNames.clear();
                         selectedNames.add(nameSelected);
                    }

                    if (selectedNames.size() > 1)
                    {
                         deleteSelectedAttributes();
                    }
               }

               glcanvas.repaint();
          }    
          else if (nameSelected <= (-100))
          {
               bondVectorSelected = true;

               clearDataFields();

               if (cntrlKeyDown)
               {
                    if ( (selectedNames.size() > 0) && (selectedNames.contains(nameSelected)) )
                    {
                         index = selectedNames.indexOf(nameSelected);
                         selectedNames.remove(index);
                    }
                    else
                    {
                         selectedNames.add(nameSelected);
                    }
               }
               else if (!cntrlKeyDown)
               {
                    if ( (selectedNames.size() > 0) && (selectedNames.contains(nameSelected)) )
                    {
                         index = selectedNames.indexOf(nameSelected);
                         selectedNames.remove(index);
                    }
                    else
                    {
                         selectedNames.clear();
                         selectedNames.add(nameSelected);
                    }
               }

               glcanvas.repaint();
          }
          else if (nameSelected < 0)
          {
               bondVectorSelected = false;

               if (nhits == 0)
               {
                    if ( (!bondLengthTxtFldSelected) && (!bondAngleTxtFldSelected) && (!dihedralTxtFldSelected) )
                    {
                         if (selectedNames.size() == 0)
                         {
                              clearDataFields();
                         }
                    }

                    editMode = getEditMode();
                    draggedMouse = getMouseDraggedState();
                    if (editMode) 
                    {
                         if (bondAngleSelected)
                         {
                             double finalBondAngle; 

                             textFlag = 3;

                              if (attributeModified == 0)
                              {
                                   selectedComponents = getSelectedComponents();
                                   writeSelectedAttributes(selectedComponents, textFlag);
                              }
                              else if ( (attributeModified == 2) && (bondAngleTxtFldSelected) )
                              {
                                   finalBondAngle = Double.parseDouble(bondAngleField.getText());
                                   bondAngleField.setText(Double.toString(finalBondAngle));

                                   deltaEpsilon = finalBondAngle - initBondAngle;

                                   if (deltaEpsilon != 0.0)
                                   {
                                        rotateFragments(attributeModified, deltaEpsilon);
                                   }

                                   initBondAngle = finalBondAngle; 

                                   bondAngleTxtFldSelected = false;
                              }
                         }
                         else if (dihedralAngleSelected)
                         {
                              double finalDihedralAngle; 

                              textFlag = 4;

                              if (attributeModified == 0)
                              {
                                   selectedComponents = getSelectedComponents();
                                   writeSelectedAttributes(selectedComponents, textFlag);
                              }
                              else if ( (attributeModified == 3) && (dihedralTxtFldSelected) )
                              {
                                   strDihedralAngle = dihedralAngleField.getText();
                                   dihedralAngleField.setText(strDihedralAngle);

                                   finalDihedralAngle = Double.parseDouble(dihedralAngleField.getText());
                                   bondAngleField.setText(Double.toString(finalDihedralAngle));

                                   deltaEta = finalDihedralAngle - initDihedralAngle;

                                   if (deltaEta != 0.0)
                                   {
                                        rotateFragments(attributeModified, deltaEta); 
                                   }
                                   
                                   dihedralTxtFldSelected = false;
                              }   
                         }
                         else if (axialRotationSelected)
                         {
                              if (attributeModified == 4)
                              {                              
                                   rotateFragments(attributeModified, delAxialAngle);  
                              }
                         }
                    }
                    else if (!editMode)
                    {
                         if (bondAngleSelected)          
                         {
                              bondAngleSelected = false;
                         }
                         else if (dihedralAngleSelected)
                         {
                              dihedralAngleSelected = false;
                         }
                         else if ( (!bondAngleSelected) && (!dihedralAngleSelected) )
                         {
                              selectedNames.clear();
                         }
                    }               
               }
          }

          glcanvas.repaint();
     }

//     public class NanocadDisplay implements GLEventListener, MouseMotionListener
     public class ParamcadDisplay implements GLEventListener, MouseMotionListener
     {
          public void init(GLDrawable gldraw)
          {
               gl = gldraw.getGL();
               glu = gldraw.getGLU();

               gl.glEnable(GL.GL_DEPTH_TEST);
               gl.glShadeModel(GL.GL_SMOOTH);
               gl.glEnable(GL.GL_POLYGON_SMOOTH);
               gl.glEnable(GL.GL_BLEND);
               gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
               gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

               bufferSize = 25;

               selectMode = 0;

               ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BufferUtils.SIZEOF_INT*bufferSize);
               byteBuffer.order(ByteOrder.nativeOrder());
               selectionBuffer = byteBuffer.asIntBuffer();

               bkg_red = 0.0f;
               bkg_green = 0.0f;
               bkg_blue = 0.0f;
               color_alphaValue = 1.0f;

               canvas2frameRatio = 0.8;

               // Read in geometry from JTextArea of Molecular Specification frame
               strGeom = new String();

               // Use String Buffer for now to construct geometry specification
               StringBuffer sbGeom = new StringBuffer();



               // BiCl3
               sbGeom.append("Bi" + "\n" + "Cl 1 rBiCl" + "\n" + "Cl 1 rBiCl  2 aClBiCl" + "\n" + "Cl 1 rBiCl 2 aClBiCl 3 aClBiCl" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rBiCl=2.48" + "\n" + "aClBiCl=99.0" + "\n");

               strGeom = sbGeom.toString();
 
               // Parse geometry specification string, convert each line separate of geometry input to an Array List 
               // (alGeomInputLine), and place each Array List not containing an "=" into a second Array List (alGeom)   
               String geomToken, var, value;
               ArrayList<String> alGeomElements; ArrayList<String> alElement, alElem3, alElem4;

               alGeom = new ArrayList<ArrayList>();
 
               StringTokenizer stok1, stok2;

               stok1 = new StringTokenizer(strGeom, "\n");

               while(stok1.hasMoreTokens())
               {
                    geomToken = stok1.nextToken();

                    if (geomToken.indexOf("=") == -1)
                    {
                         alGeomElements = new ArrayList<String>(); 

                         stok2 = new StringTokenizer(geomToken);                     
                
                         while(stok2.hasMoreTokens())
                         {
                              alGeomElements.add(stok2.nextToken());
                         }

                         alGeom.add(alGeomElements);
                    }
                    else
                    {
                         stok2 = new StringTokenizer(geomToken, "=");             

                         var = stok2.nextToken();
                         value = stok2.nextToken();                         

                         for (index = 0; index < alGeom.size(); index++)
                         {
                              alElement = new ArrayList<String>(); 
                              alElement = (ArrayList)(alGeom.get(index));

                              if (alElement.contains(var))
                              {
                                   for (int ndx = 0; ndx < alElement.size(); ndx++)
                                   {
                                        k = alElement.indexOf(var);

                                        if (k != -1)
                                        {
                                             alElement.set(k, value);
                                        }
                                        else
                                        {
                                             break;
                                        }
                                   }

                                   alGeom.set(index, alElement);
                              }
                         }
                    }
               }

               natoms = alGeom.size();

               coordFormat = getCoordFormat(alGeom, natoms);

               // Convert ZMatrix to Cartesian coordinate format

               atomCoords = new double[3*natoms];
               atomCoords = getAtomCoords(alGeom, natoms, coordFormat);

               // Get identity of atoms in the molecule being displayed
               atomID = new String[natoms];
               atomID = getAtomID(alGeom, natoms);

               elemID = new ArrayList<String>();

               // Get atomic number for each atom in the molecule being displayed
               atomicNumber = new int[natoms];
               atomicNumber = getAtomicNumber(atomID, natoms);

               // Get bond lengths between bonded atoms in the molecule being displayed
               // Note that this also generates an atom connectivity array
               zmatBondLength = new double[natoms][natoms];

               if (coordFormat.equals("zmat"))
               {
                    zmatBondLength = getZMatBondLength(alGeom, natoms);
               }
               else
               {
                    // Initialize zmatBondLength matrix elements to zero
                    for (j = 0; j < natoms; j++)
                    {
                         for (k = 0; k < natoms; k++)
                         {
                              zmatBondLength[j][k] = 0.0;
                         }
                    }
               }

               // Set default bonding parameter
               bondParam = 1.0;

               // Set recomputeConnectivity  
               recomputeConnectivity = false;               

               // Get complete connectivity for this molecule 

               if (natoms > 1)
               {
                    bondLength = new double[natoms][natoms];
                    bondLength = getBondLength(zmatBondLength, natoms, atomCoords, atomicNumber, bondParam);
               }

               // Set default atom and bond radii
               defaultAtomRadius = 0.25;
               bondRadius = 0.075;

               scaledAtomRadius = new double[natoms];
               
               if (natoms > 1)
               {
                    unscaledAtomRadius = getUnscaledAtomRadius(bondLength, natoms);
                    scaledAtomRadius = getScaledAtomRadius(atomicNumber, unscaledAtomRadius, natoms);
               }
               else
               {
                    unscaledAtomRadius = defaultAtomRadius;
                    scaledAtomRadius[0] = defaultAtomRadius;
               }

               // Scale atom radii by default
               scaleRadius = 1;       // 0 (unscaled), 1 (scaled), 2 (wireframe)

               // Set default scale factor
               scaleFactor = 1.0;

               // Get color of each atom in the molecule being displayed
               atomColors = new float[3*natoms];
               atomColors = getAtomColors(atomicNumber, natoms);

               // Set bonds to be visible by default (bondsVisible = true)
               bondsVisible = true;

               // Set bonds to be uncolored by default (bondsColored = false)
               bondsColored = false;

               bondMat1 = new float[(3*natoms)];
               bondMat2 = new float[(3*natoms)];

               // Initialize angles specifying molecule's rotation
               theta = phi = psi = 0.0;
               delTheta = delPhi = delPsi = 7.5;

               mux = muy = muz = 0.0; 
//               delMu = 5.0;
//               delMu = 0.025;
               delMu = 0.001;

               // Initialize angles specifying rotation of a selected molecular fragment
               alpha = beta = omega = 0.0;
               delAlpha = delBeta = delOmega = 5.0;

               // Initialize angles specifying bond angle (epsilon) and dihedral angle (eta) rotation
               epsilon = eta = 0.0;
               delEpsilon = 0.075; 
               delEta = 0.075;         // delEta = 2.0;

               deltaDihedral = 0.5;
               delAxialAngle = 0.075;

               bvConnTolAngle = 2.0;  bondConnTolAngle = 5.0;

               // Set origin of coordinate reference frame
               x0 = 0.0;
               y0 = 0.0;
               z0 = 0.0;
               
               // Set distance of coordinate frame origin from camera
               zloc = 7.0;

               x1 = 0.0;
               y1 = 0.0;
 
               x_rot = false;
               y_rot = false;
               z_rot = false;

               nameSelected = -1;

               // Set initial display modes
               drawMode = false;
               editMode = false; editModeOn = false;
               drawAtomMode = false;
               editMoleculeMode = false;
               joinFragments = false;
               addSolventH2O = false;
               displayMode = false;
               displayFragment = false;
               drawBondVectors = false;
               compatBndOrdr = false;

               sketchAtom = drawAtom = false;
               sketchBond = drawBond = false;
               sketchFcnalGrp = drawFcnalGrp = false;
               displayStructure = false;
               replaceAtom = false;
               importStructureFile = false;
               selectedElement = false;
               timerRunning = false;

               selectedFF = null;

               natoms = nbonds = fragmentNum = nsubstructures = 0; 
               nfragatoms = nfragbonds = 0;
               nuserDefMolStructures = 0;
               maxfragID = 0;
               ncoords = 3;
               ncoordCols = 5;
               nbvCols = 6;
               nnewbvCols = 6;
               ndims = ndimensions = 3;
               ncolors = 3;
               nclicks = 0;
               molScaleFactor = canvasScaleFactor = combScaleFactor = 1.0;
               numMouseClicks = 0;
               rotType = 0;
               rotOriginNdx = (-1);
               defaultBondLength = 0;
               attributeModified = 0;
               initBondLngth = initBondAngle = initDihedralAngle = 0.0;
               ang2pixls = 0.0198295;
               maxMolDimen = 0.0;

               nbondAngles = ndihedrals = 0;

//               natomCutoff = 30;  rotAccel = 0.025;  msfAccel = 0.0005;
               natomCutoff = 30;  msfAccel = 0.002;
               //rotAccel = 0.020;
               rotAccel = 0.1;


               fragFileID = 0;
//               userDefFileNumbr = 1000;
               userDefFileSuffix = 0;
               hbDonorNdx = hbAcceptorNdx = (-1);
               solventOrientNum = 0;
               currentlySelectedAtomNdx = (-1);

               // Set key pressed events to false
               arrowKeyDown = false;
               altKeyDown = cntrlKeyDown = deleteKeyDown = false; 
               escapeKeyDown = shiftKeyDown = spaceKeyDown = false;
               tKeyDown = xKeyDown = yKeyDown = zKeyDown = xyzKeyDown = false;
               selectionComplete = false;
               pressmouse = releasemouse = false;
               mouseWasDragged = false;
               RMBclicked = false;
               eraseMolecule = false;
               writeBondLength = isBondLength = isBondAxis = isBondAngle = isDihedralAngle = false;
               bondVectorSelected = false;
               bondAngleSelected = dihedralAngleSelected = false; 
               bvDihedralSelected = false;
               axialRotationSelected = false;
               rotationOriginIndex = (-1);
               addBond = addElement = addFragment = addFcnalGrp = false;
               fragmentSelected = false;
               translateAtoms = translateFragment = false;
               rotFragment = rotBondAngle = false;
               mouseButtonPressed = mouseButtonClicked = 0;
               bondLengthTxtFldSelected = bondAngleTxtFldSelected = dihedralTxtFldSelected = false;
               undo = redo = false;
               releasedMouse = false;
               forcefieldSelected = paramFrameVisible = false;
               hbDonor = hbAcceptor = false;
               trigonalplanar = linear = false;
               atomSolvated = false;

idCentralDhBond = false;

               msec1 = msec2 = 0;

               axisEndPoints1 = new double[ndimensions][ndimensions];
               axisEndPoints2 = new double[ndimensions][ndimensions];

               // Initialize both axisEndPoints arrays
               axisEndPoints1[0][0] = axisEndPoints1[1][1] = axisEndPoints1[2][2] = 1.0;

               axisEndPoints1[0][1] = axisEndPoints1[0][2] = 0.0;
               axisEndPoints1[1][0] = axisEndPoints1[1][2] = 0.0;
               axisEndPoints1[2][0] = axisEndPoints1[2][1] = 0.0;

               for (j = 0; j < ndimensions; j++)
               {
                    for (k = 0; k < ndimensions; k++)
                    {
                         axisEndPoints2[j][k] = axisEndPoints1[j][k];
                    }
               }


//globalAxisRotation = xyzKeysDepressed = rotatedStructure = false;
globalAxisRotation = rotatedStructure = false;
globalRotationAxis = new double[ncoords];

globalRotationAxes = new double[ncoords];
globalRotationAxes[0] = 1.0;  globalRotationAxes[1] = 1.0;  globalRotationAxes[2] = 1.0;  



               fragmentAtoms = new ArrayList<Integer>();
               selectedFragmentArray = new ArrayList<Integer>();
               selectedFragments = new ArrayList<Integer>();
               AN = new ArrayList<Integer>();
               orderedAtomListIndices = new ArrayList<Integer>();
               bvID = new ArrayList<Integer>();
               translatedAtomIndices = new ArrayList<Integer>();
               coords = new ArrayList<Double>();
               fragmentOrigin = new ArrayList<Double>();
               atomRadii = new ArrayList<Double>();
               rotationAxis = new ArrayList<Double>();
               atomCharges = new ArrayList<Double>();
               usrdefTheta = new ArrayList<Double>();
               usrdefPhi = new ArrayList<Double>();
               usrdefPsi = new ArrayList<Double>();
               componentElements = new ArrayList<String>();
               orderedElements = new ArrayList<String>();
               orderedAtomList = new ArrayList<String>();
               atomNames = new ArrayList<String>();


albondID = new ArrayList<Integer>();
alKb = new ArrayList<Double>();
alR0 = new ArrayList<Double>();

alAtomNames = new ArrayList<String>();
atomName1_bonds = new ArrayList<String>();  atomName2_bonds = new ArrayList<String>();

               atomTypes = new ArrayList<String>();

               bondAtomTypes = new ArrayList<String>();
               angleAtomTypes = new ArrayList<String>();
               dhAtomTypes = new ArrayList<String>();
               imprAtomTypes = new ArrayList<String>();

               alAtomTypes = new ArrayList<String>();
               atomType1_bonds = new ArrayList<String>();  
               atomType2_bonds = new ArrayList<String>();

               // Initialize array list for H-bond acceptors: add electronegative atoms N, O, F, and Cl
               Hacceptors = new ArrayList<String>();
               Hacceptors.add("N");  Hacceptors.add("O");  Hacceptors.add("F"); Hacceptors.add("Cl");
 
               moleculeRTI = new ArrayList<String>(); 
               atomRTI = new ArrayList<String>();
               bondRTI = new ArrayList<String>();
               substructRTI = new ArrayList<String>();
               newColor = new ArrayList<Float>();
               alAtomRTI = new ArrayList<ArrayList<String>>();
               alBondRTI = new ArrayList<ArrayList<String>>();
               elemID = new ArrayList<String>();
               fragElemID = new ArrayList<String>();
               selectedComponents = new ArrayList<Integer>();
               bondAngleFragments = new ArrayList<ArrayList<Integer>>();
               bondAngleFragments1 = new ArrayList<Integer>();
               bondAngleFragments2 = new ArrayList<Integer>();
               dihedralAngleFragments = new ArrayList<Integer>();
               axialFragmentAtoms = new ArrayList<Integer>();
               fragment1BV = new ArrayList<Integer>();
               fragment2BV = new ArrayList<Integer>();
               fragmentBV = new ArrayList<Integer>();
               userDefnbvs = new ArrayList<Integer>();

               alDihedralAtoms = new ArrayList<Integer>();

               rowndxList = new ArrayList<Integer>();
               selectedRowNdxList = new ArrayList<Integer>();
               dhCentralBondIndices = new ArrayList<Integer>();
               minDHCentralBondIndices = new ArrayList<Integer>();
               maxDHCentralBondIndices = new ArrayList<Integer>();

               dhParamSource = new ArrayList<String>();
               dhMult = new ArrayList<Integer>();
               dhKforce = new ArrayList<Double>();
               dhPhaseAngle = new ArrayList<Double>();

               QPenalty = new ArrayList<Double>(); 
               bondPenalty = new ArrayList<Double>();
               bondAnglePenalty = new ArrayList<Double>();
               dhPenalties = new ArrayList<Double>();
               imprPenalty = new ArrayList<Double>();

               selectedDihedrals = new ArrayList<String>();

               mouseTblClicks = 0;

               natomsSketched = 0;
               numAtoms = 0;
               XX = YY = 0;

               bvConnections = new int[0][0];
               solvatedAtomIndices = new int[2];
               delCoords = new double[ncoords];
               rotOrigin = new double[ncoords];
               mouseDownCoords = new double[2];
               mouseUpCoords = new double[2];
               delMouseXY = new double[2];
               bondVectors = new double[0][0];

               selectedNames = new ArrayList<Integer>();
               selectedFragmentAtoms = new ArrayList<Integer>();
               xyzKeys = new ArrayList<Integer>(3);

               for (ndx = 0; ndx < 3; ndx++)
               {
                    xyzKeys.add(0); 
               }

               // Headings for writing mol2 file 
               mol2Heading1 = "@<TRIPOS>MOLECULE\n";  mol2Heading2 = "@<TRIPOS>ATOM\n";
               mol2Heading3 = "@<TRIPOS>BOND\n";      mol2Heading4 = "@<TRIPOS>SUBSTRUCTURE\n";

               filename = new String();

               moleculeName = new String();
               mol2filename = new String();
               currentMol2Filename = new String();
               bvfilename = new String();

               selectedForceField = new String();

////////////

// Initially we are making CGenFF the default force field
selectedForceField = "CGenFF"; 

////////////   

               strBondAngle = new String();
               strDihedralAngle = new String();

               hybridization = new String();
               elemFieldText = new String();
               hbDonorID = new String();
               hbAcceptorID = new String();

               defaultBondLength = 0.0;
               bondLngth = 0.0;
               elementSelected = "C (sp3)";       // default 
               selectedElem = "C";                // default 
               atomnumbr = 6;                     // default 
               nbondVectors = 4;                  // default 
               hybridization = "sp3";             // default
               displayStyle = "ball_and_stick";   // default

               displayedFile = null;

rotAxis = "0";
nxx1 = 1.0; nxx2 = 0.0; nxx3 = 0.0;
nyy1 = 0.0; nyy2 = 1.0; nyy3 = 0.0;
nzz1 = 0.0; nzz2 = 0.0; nzz3 = 1.0;

rotationAxisID = "0";
keyID = "0";
nxx = nyy = nzz = 1.0;
globalRotationAxes = new double[ncoords];

deltaAngle = 0.0;

globalRotationAxes[0] = nxx;   globalRotationAxes[1] = nyy;  globalRotationAxes[2] = nzz;

          }

          public String[] getAtomID(ArrayList<ArrayList> alGeom, int natoms)
          {
               int indx;

               Character chr;

               ArrayList listElement;

               StringBuffer sbSymbolID;

               String indexedAtoms;

               String strSymbolID;
               String[] symbolID = new String[natoms];   
 
//               String[] atomicSymbol = {"H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg"};
               String[] elementName = {"Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminum", "Silicon", "Phosphorus", "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver", "Cadmium", "Indium", "Tin", "Antimony", "Tellurium", "Iodine", "Xenon", "Cesium", "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodynium", "Promethium", "Samarium", "Europium","Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium", "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum", "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon", "Francium", "Radium", "Actinum", "Thorium", "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium", "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium"};

               for (j = 0; j < natoms; j++)
               {
                    listElement = new ArrayList();
                    listElement = (ArrayList)(alGeom.get(j));

                    strSymbolID = (listElement.get(0)).toString();

                    sbSymbolID = new StringBuffer();
                    
                    for (k = 0; k < strSymbolID.length(); k++)
                    {
                         chr = new Character(strSymbolID.charAt(k));

                         if (Character.isLetter(chr))
                         {
                              sbSymbolID.append(chr.toString());
                         }
                    }

                    if (sbSymbolID.length() > 2)
                    {
                         for (indx = 0; indx < elementName.length; indx++)
                         {
                              if ( (sbSymbolID.toString()).equals(elementName[indx]) )
                              {
                                   strSymbolID = atomicSymbol[indx];

                                   break;
                              }
                         }
                    }
                    else
                    {
                         strSymbolID = sbSymbolID.toString();
                    }

                    symbolID[j] = strSymbolID;
               }

               return symbolID;         
          }

          public String getCoordFormat(ArrayList<ArrayList> alGeom, int natoms)
          {
               String geomFormat = new String();

               ArrayList elemList01,elemList02,elemList03,elemList04;

               switch(natoms)
               {
                    case 0: geomFormat = "undefined";
                            break;
                    case 1: elemList01 = new ArrayList();
                            elemList01 = (ArrayList)alGeom.get(0);

                            if (elemList01.size() == 1)
                            {
                                 geomFormat = "zmat";
                            }
                            else if (elemList01.size() > 1)
                            {
                                 geomFormat = "cartesian";
                            }
                            break;

                    case 2: elemList01 = new ArrayList();
                            elemList02 = new ArrayList();

                            elemList01 = (ArrayList)alGeom.get(0);
                            elemList02 = (ArrayList)alGeom.get(1);

                            if ((elemList01.size() == 1) && (elemList02.size() == 3) )
                            {
                                 geomFormat = "zmat";
                            }
                            else if ((elemList01.size() > 1) || (elemList02.size() == 5) )
                            {
                                 geomFormat = "cartesian";
                            }
                            break;

                    case 3: elemList02 = new ArrayList();
                            elemList03 = new ArrayList();

                            elemList02 = (ArrayList)alGeom.get(1);
                            elemList03 = (ArrayList)alGeom.get(2);

                            if ((elemList02.size() == 3) && (elemList03.size() == 5) )
                            {
                                 geomFormat = "zmat";
                            }
                            else if ((elemList02.size() == 5) || (elemList03.size() == 5) )
                            {
                                 geomFormat = "cartesian";
                            }

                            break;

                   default: elemList03 = new ArrayList();
                            elemList04 = new ArrayList();

                            elemList03 = (ArrayList)alGeom.get(2);
                            elemList04 = (ArrayList)alGeom.get(3);

                            if ((elemList03.size() == 5) && (elemList04.size() >= 7) )
                            {
                                 geomFormat = "zmat";
                            }
                            else if ((elemList03.size() == 5) || (elemList04.size() == 5) )
                            {
                                 geomFormat = "cartesian";
                            }

                            break;
               }
               return geomFormat;    
          }

          public double[] getAtomCoords(ArrayList<ArrayList> alGeom, int natoms, String coordFormat)
          {
               int bondedAtomIndex, alphaAtomIndex, gammaAtomIndex;
               double bd, dx, dy, dz, dx1, dy1, dz1, dx2, dy2, dz2, r, nx, ny, nz, n, alpha, gamma;
               double[] prerotCoord = new double[3];
               double[] coords = new double[3*natoms];    
               double[] rotCoord = new double[3];    
               double[][] rotMatrix = new double[3][3];    
               ArrayList elemList;
               
               if (coordFormat.equals("cartesian"))
               {
                    for (j = 0; j < natoms; j++)
                    {
                         elemList = new ArrayList(); 
                         elemList = (ArrayList)alGeom.get(j);

                         if (elemList.size() == 2)
                         {
                              for (k = 0; k < 3; k++)
                              {
                                   coords[(3*j + k)] = 0.00;
                              }
                         }
                         else if (elemList.size() == 3)
                         {
                              coords[(3*j)] = Double.parseDouble((elemList.get(2)).toString() );

                              for (k = 1; k < 3; k++)
                              {
                                   coords[(3*j + k)] = 0.00;
                              }
                         }
                         else if (elemList.size() == 4)
                         {
                              coords[(3*j)] = Double.parseDouble((elemList.get(2)).toString() );
                              coords[(3*j) + 1] = Double.parseDouble((elemList.get(3)).toString() );
                              coords[(3*j) + 2] = 0.00; 
                         }
                         else if (elemList.size() == 5)
                         {
                              for (k = 0; k < 3; k++)
                              {
                                   coords[(3*j + k)] = Double.parseDouble((elemList.get(k + 2) ).toString() );
                              }
                         }
                    }
               }

               if (coordFormat.equals("zmat"))
               {
                    double dx12, dy12, dz12, dx23, dy23, dz23, dx34, dy34, dz34;
                    double r11, r22, r33, R;
                    double rx, ry, rz;
                    double n11, n22, n33;
                    double nx_prime, ny_prime, nz_prime;

                    int atom1Index, atom2Index, atom3Index, atom4Index; 


                    for (index = 0; index < natoms; index++)
                    {
                         elemList = new ArrayList();
       
                         switch(index)
                         {
                              case 0: coords[(3*index)] = 0.0;
                                      coords[(3*index) + 1] = 0.0;
                                      coords[(3*index) + 2] = 0.0;
                                      break;

                              case 1: elemList = (ArrayList)alGeom.get(index);
                                      bd = Double.parseDouble( (elemList.get(2)).toString() );
                                      coords[(3*index)] = 0.0;
                                      coords[(3*index) + 1] = 0.0;
                                      coords[(3*index) + 2] = bd;
                                      break;

                              case 2: elemList = (ArrayList)alGeom.get(index);                          

//////////////////////////
//                      //
//               o 3    //
//              /       //
//             /        //
//   1 o ---- o 2       //
//                      //
//////////////////////////

                                      atom3Index = index;
                                      atom2Index = (Integer.parseInt( (elemList.get(1)).toString() )) - 1;
                                      atom1Index = (Integer.parseInt( (elemList.get(3)).toString() )) - 1;

                                      // Compute difference in coordinates between atom 1 and atom 2, R12
                                      dx12 = coords[(3*atom2Index)] - coords[(3*atom1Index)];
                                      dy12 = coords[(3*atom2Index) + 1] - coords[(3*atom1Index) + 1];
                                      dz12 = coords[(3*atom2Index) + 2] - coords[(3*atom1Index) + 2];

                                      // Compute unit vector along bond from atom 2 to atom 1
                                      r = Math.sqrt( (dx12*dx12) + (dy12*dy12) + (dz12*dz12) );
                                     
                                      nx = -dx12/r;
                                      ny = -dy12/r;
                                      nz = -dz12/r;

                                      // Compute unit vector along axis of rotation (alpha angle) centered on atom 2
                                      n11 = -1.0;
                                      n22 = 0.0;
                                      n33 = 0.0;

                                      // Retrieve alpha rotation angle
                                      alpha = Double.parseDouble( (elemList.get(4)).toString() );

                                      // (1) Convert bond angle (alpha) from degrees to radians
                                      alpha = alpha * (Math.PI/180.0);

                                      // Compute rotation matrix
                                      rotMatrix[0][0] = (n11 * n11) + ( Math.cos(alpha) * (1 - n11 * n11) );
                                      rotMatrix[0][1] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) + (n33 * Math.sin(alpha));
                                      rotMatrix[0][2] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) - (n22 * Math.sin(alpha));

                                      rotMatrix[1][0] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) - (n33 * Math.sin(alpha));
                                      rotMatrix[1][1] = (n22 * n22) + ( Math.cos(alpha) * (1 - n22 * n22) );
                                      rotMatrix[1][2] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) + (n11 * Math.sin(alpha));
 
                                      rotMatrix[2][0] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) + (n22 * Math.sin(alpha));
                                      rotMatrix[2][1] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) - (n11 * Math.sin(alpha));
                                      rotMatrix[2][2] = (n33 * n33) + ( Math.cos(alpha) * (1 - n33 * n33) );
       
                                      // (3) Compute rotated unit vector along bond from atom 2 to atom 3
                                      nx_prime = rotMatrix[0][0] * nx + rotMatrix[0][1] * ny + rotMatrix[0][2] * nz;  
                                      ny_prime = rotMatrix[1][0] * nx + rotMatrix[1][1] * ny + rotMatrix[1][2] * nz;  
                                      nz_prime = rotMatrix[2][0] * nx + rotMatrix[2][1] * ny + rotMatrix[2][2] * nz;  
                                   
                                     // Compute final cartesian coordinates for this atom 
                                      bd = Double.parseDouble( (elemList.get(2)).toString() ); 

                                     coords[(3*index)] = coords[(3*atom2Index)] + (nx_prime * bd);  
                                     coords[(3*index) + 1] = coords[(3*atom2Index) + 1] + (ny_prime * bd);  
                                     coords[(3*index) + 2] = coords[(3*atom2Index) + 2] + (nz_prime * bd);  
                                    
                                     break;

                             default: elemList = (ArrayList)alGeom.get(index);

/////////////////////////////
//                         //
//                  o 4    //
//                 /       //
//                /        //
//      2 o ---- o 3       //
//       /                 //
//      /                  //
//   1 o                   //
//                         //  
/////////////////////////////

                                      atom4Index = index;
                                      atom3Index = (Integer.parseInt( (elemList.get(1)).toString() )) - 1;
                                      atom2Index = (Integer.parseInt( (elemList.get(3)).toString() )) - 1;
                                      atom1Index = (Integer.parseInt( (elemList.get(5)).toString() )) - 1;

                                      // Compute difference in coordinates between atom 1 and atom 2, R12
                                      dx12 = coords[(3*atom2Index)] - coords[(3*atom1Index)];
                                      dy12 = coords[(3*atom2Index) + 1] - coords[(3*atom1Index) + 1];
                                      dz12 = coords[(3*atom2Index) + 2] - coords[(3*atom1Index) + 2];

                                      // Compute difference in coordinates between atom 2 and atom 3, R23
                                      dx23 = coords[(3*atom3Index)] - coords[(3*atom2Index)];
                                      dy23 = coords[(3*atom3Index) + 1] - coords[(3*atom2Index) + 1];
                                      dz23 = coords[(3*atom3Index) + 2] - coords[(3*atom2Index) + 2];
                                      
                                      // Compute cross-product R12 x R23 = [ (dx12, dy12, dz12) X (dx23, dy23, dz23) ]
                                      rx = (dy12 * dz23) - (dy23 * dz12);
                                      ry = (dz12 * dx23) - (dz23 * dx12);
                                      rz = (dx12 * dy23) - (dx23 * dy12);

                                      // Compute unit vector along axis of rotation (alpha angle) normal to plane of atoms 1, 2, and 3 and centered on atom 3
                                      r = Math.sqrt(rx*rx + ry*ry + rz*rz);

                                      n11 = rx/r;
                                      n22 = ry/r;
                                      n33 = rz/r;

                                      // Compute unit vector along bond from atom 3 to atom 2
                                      r = Math.sqrt( (dx23*dx23) + (dy23*dy23) + (dz23*dz23) );
                                     
                                      nx = -dx23/r;
                                      ny = -dy23/r;
                                      nz = -dz23/r;

                                      // Retrieve alpha rotation angle
                                      alpha = Double.parseDouble( (elemList.get(4)).toString() );

                                      // (1) Convert bond angle (alpha) from degrees to radians
                                      alpha = alpha * (Math.PI/180.0);

                                      // Compute rotation matrix
                                      rotMatrix[0][0] = (n11 * n11) + ( Math.cos(alpha) * (1 - n11 * n11) );
                                      rotMatrix[0][1] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) + (n33 * Math.sin(alpha));
                                      rotMatrix[0][2] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) - (n22 * Math.sin(alpha));

                                      rotMatrix[1][0] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) - (n33 * Math.sin(alpha));
                                      rotMatrix[1][1] = (n22 * n22) + ( Math.cos(alpha) * (1 - n22 * n22) );
                                      rotMatrix[1][2] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) + (n11 * Math.sin(alpha));
 
                                      rotMatrix[2][0] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) + (n22 * Math.sin(alpha));
                                      rotMatrix[2][1] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) - (n11 * Math.sin(alpha));
                                      rotMatrix[2][2] = (n33 * n33) + ( Math.cos(alpha) * (1 - n33 * n33) );
       
                                      // (3) Compute rotated unit vector along bond from atom 2 to atom 3
                                      nx_prime = rotMatrix[0][0] * nx + rotMatrix[0][1] * ny + rotMatrix[0][2] * nz;  
                                      ny_prime = rotMatrix[1][0] * nx + rotMatrix[1][1] * ny + rotMatrix[1][2] * nz;  
                                      nz_prime = rotMatrix[2][0] * nx + rotMatrix[2][1] * ny + rotMatrix[2][2] * nz;  
                                   
                                      // Retrieve gamma rotation angle (dihedral angle)
                                      gamma = Double.parseDouble( (elemList.get(6)).toString() );

                                      // (1) Convert dihedral angle (gamma) from degrees to radians
                                      gamma = gamma * (Math.PI/180.0);

                                      // Compute unit vector along axis of rotation (dihedral angle) = unit vector along bond from atom 3 to atom 2
                                      n11 = -dx23/r;
                                      n22 = -dy23/r;
                                      n33 = -dz23/r;

                                      // Compute rotation matrix
                                      rotMatrix[0][0] = (n11 * n11) + ( Math.cos(gamma) * (1 - n11 * n11) );
                                      rotMatrix[0][1] = ( (n11 * n22) * (1 - Math.cos(gamma)) ) + (n33 * Math.sin(gamma));
                                      rotMatrix[0][2] = ( (n33 * n11) * (1 - Math.cos(gamma)) ) - (n22 * Math.sin(gamma));

                                      rotMatrix[1][0] = ( (n11 * n22) * (1 - Math.cos(gamma)) ) - (n33 * Math.sin(gamma));
                                      rotMatrix[1][1] = (n22 * n22) + ( Math.cos(gamma) * (1 - n22 * n22) );
                                      rotMatrix[1][2] = ( (n22 * n33) * (1 - Math.cos(gamma)) ) + (n11 * Math.sin(gamma));
 
                                      rotMatrix[2][0] = ( (n33 * n11) * (1 - Math.cos(gamma)) ) + (n22 * Math.sin(gamma));
                                      rotMatrix[2][1] = ( (n22 * n33) * (1 - Math.cos(gamma)) ) - (n11 * Math.sin(gamma));
                                      rotMatrix[2][2] = (n33 * n33) + ( Math.cos(gamma) * (1 - n33 * n33) );

                                      // Compute unit vector along bond from atom 3 to atom 4
                                      nx = nx_prime;
                                      ny = ny_prime;
                                      nz = nz_prime;

       
                                      // Compute rotated unit vector along bond from atom 3 to atom 4
                                      nx_prime = rotMatrix[0][0] * nx + rotMatrix[0][1] * ny + rotMatrix[0][2] * nz;  
                                      ny_prime = rotMatrix[1][0] * nx + rotMatrix[1][1] * ny + rotMatrix[1][2] * nz;  
                                      nz_prime = rotMatrix[2][0] * nx + rotMatrix[2][1] * ny + rotMatrix[2][2] * nz;  
  

                                     // Compute final cartesian coordinates for this atom 
                                      bd = Double.parseDouble( (elemList.get(2)).toString() ); 

                                     coords[(3*index)] = coords[(3*atom3Index)] + (nx_prime * bd);  
                                     coords[(3*index) + 1] = coords[(3*atom3Index) + 1] + (ny_prime * bd);  
                                     coords[(3*index) + 2] = coords[(3*atom3Index) + 2] + (nz_prime * bd);  
                                    
                                     break;
                         }
                    }
               }

               return coords;
          }

          public double[][] getZMatBondLength(ArrayList<ArrayList> alGeom, int natoms)
          {
               ArrayList listElem;
               int bondedAtomIndex;
               double[][] bondLen;

               bondLen = new double[natoms][natoms];

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < natoms; k++)
                    {
                         bondLen[j][k] = 0.0;
                    }
               }

               for (j = 1; j < natoms; j++)
               {
                    listElem = new ArrayList();
                    listElem = (ArrayList)alGeom.get(j);

                    bondedAtomIndex = (Integer.parseInt( (listElem.get(1)).toString() )) - 1;

                    bondLen[j][bondedAtomIndex] = Double.parseDouble( (listElem.get(2)).toString() );
               }

               return bondLen;
          }

          public double[][] getBondLength(double[][] zmatBondLength, int natoms, double[] atomCoords, int[] atomicNumber, double bondParam)
          {
               double dx, dy, dz, r, bl;    
               double ar1, ar2, cr1, cr2;               

               // VFI atomic radii (from CrystalMaker 1.4.0)
               double[ ] ar  =  { 0.46, 1.22, 1.55, 1.13, 0.91, 0.77, 0.71, 0.48, 0.42, 1.60, 1.89, 1.60, 1.43, 1.34, 1.30, 0.88, 0.79, 1.92, 2.36, 1.97, 1.64, 1.46, 1.34, 1.40, 1.31, 1.26, 1.25, 1.24, 1.28, 1.27, 1.39, 1.39, 1.48, 1.43, 0.94, 1.98, 2.48, 2.15, 1.81, 1.60, 1.45, 1.39, 1.36, 1.34, 1.34, 1.37, 1.44, 1.56, 1.66, 1.58, 1.61, 1.70, 1.15, 2.18, 2.68, 2.21, 1.87, 1.83, 1.82, 1.82, 2.05, 1.81, 2.02, 1.79, 1.77, 1.77, 1.76, 1.75, 1.75, 1.93, 1.74, 1.59, 1.46, 1.40, 1.37, 1.35, 1.35, 1.38, 1.44, 1.60, 1.71, 1.75, 1.82, 1.35, 1.27, 1.20, 1.00, 1.00, 2.03, 1.80, 1.62, 1.53, 1.50, 1.50, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };
               double[ ] covrad  =  { 0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };         

               double[][] connectivity;
               connectivity = new double[natoms][natoms];

               for (j = 1; j < natoms; j++)
               {
                    for (k = 0; k < j; k++)
                    {
                         if (zmatBondLength[j][k] > 0.0)
                         {
                              connectivity[j][k] = zmatBondLength[j][k];
                         }
                    }
               }

               for (j = 1; j < natoms; j++)
               {
                    for (k = 0; k < j; k++)
                    {
                         if (connectivity[j][k] == 0.0)
                         {
                              dx = atomCoords[(3*j)] - atomCoords[(3*k)];
                              dy = atomCoords[(3*j) + 1] - atomCoords[(3*k) + 1];
                              dz = atomCoords[(3*j) + 2] - atomCoords[(3*k) + 2];

                              r = Math.sqrt(dx*dx + dy*dy + dz*dz);

                              ar1 = ar[(atomicNumber[j] - 1)];
                              ar2 = ar[(atomicNumber[k] - 1)];

                              if ( r <= 0.4*bondParam*(ar1 + ar2) )
                              {
                                   connectivity[j][k] = r;
                              }
                         }
                    }
               }

               return connectivity;
          }

          public double getUnscaledAtomRadius(double[][] bondLength, int natoms)
          {
               double minBondLength;
               double unscaledRadius;
    
               minBondLength = 1000.0;

               for (j = 1; j < natoms; j++)
               {
                    for (k = 0; k < j; k++)
                    {
                         if ( (bondLength[j][k] != 0.0) && (bondLength[j][k] < minBondLength) )
                         {
                              minBondLength = bondLength[j][k];
                         }
                    }
               }

               unscaledAtomRadius = 0.40*minBondLength;

               return unscaledAtomRadius;
          }

          public double[] getScaledAtomRadius(int[] atomicNumber, double unscaledAtomRadius, int natoms)
          {
               double maxRadius;
               double[] scaledRadius;

               // VFI atomic radii (from CrystalMaker 1.4.0)
               double[ ] ar  =  { 0.46, 1.22, 1.55, 1.13, 0.91, 0.77, 0.71, 0.48, 0.42, 1.60, 1.89, 1.60, 1.43, 1.34, 1.30, 0.88, 0.79, 1.92, 2.36, 1.97, 1.64, 1.46, 1.34, 1.40, 1.31, 1.26, 1.25, 1.24, 1.28, 1.27, 1.39, 1.39, 1.48, 1.43, 0.94, 1.98, 2.48, 2.15, 1.81, 1.60, 1.45, 1.39, 1.36, 1.34, 1.34, 1.37, 1.44, 1.56, 1.66, 1.58, 1.61, 1.70, 1.15, 2.18, 2.68, 2.21, 1.87, 1.83, 1.82, 1.82, 2.05, 1.81, 2.02, 1.79, 1.77, 1.77, 1.76, 1.75, 1.75, 1.93, 1.74, 1.59, 1.46, 1.40, 1.37, 1.35, 1.35, 1.38, 1.44, 1.60, 1.71, 1.75, 1.82, 1.35, 1.27, 1.20, 1.00, 1.00, 2.03, 1.80, 1.62, 1.53, 1.50, 1.50, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };
               double[ ] covrad  =  { 0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };         

               scaledRadius = new double[natoms];
               maxRadius = ar[atomicNumber[0] - 1];

               for (j = 1; j < natoms; j++)
               {                
                  if (covrad[atomicNumber[j] - 1] > maxRadius)
                    {
                         maxRadius = ar[(atomicNumber[j] - 1)];
                    }
               }

               for (j = 0; j < natoms; j++)
               {   
                    scaledRadius[j] = unscaledAtomRadius * (ar[(atomicNumber[j] - 1)]/maxRadius);
               }

               return scaledRadius;
          }
                                 
          public float[] getAtomColors(int[] atomicNumber, int natoms)
          {
               float r, g, b;
               r = 0.1569f; g = 0.5098f; b = 0.9020f;

               float[] colors = new float[(3*natoms)];
               float[] atomRGB = new float[(3*natoms)];
        
               for (j = 0; j < natoms; j++)
               {
                    switch(atomicNumber[j])
                    {
                         case 1: r = 1.0000f; g = 1.0000f; b = 1.0000f;   // H
                                 break;
                         case 2: r = 0.8510f; g = 1.0000f; b = 1.0000f;   // He
                                 break;
                         case 3: r = 0.7529f; g = 0.7529f; b = 0.7529f;   // Li
                                 break;
                         case 4: r = 0.7725f; g = 1.0000f; b = 0.0000f;   // Be
                                 break;
                         case 5: r = 1.0000f; g = 0.7176f; b = 0.7176f;   // B
                                 break; 
                         case 6: r = 0.5098f; g = 0.5098f; b = 0.5098f;   // C
                                 break;
                         case 7: r = 0.0000f; g = 0.0000f; b = 1.0000f;   // N
                                 break;
                         case 8: r = 1.0000f; g = 0.0000f; b = 0.0000f;   // O
                                 break;
                         case 9: r = 0.7020f; g = 1.0000f; b = 1.0000f;   // F
                                 break;
                        case 10: r = 0.6863f; g = 0.8902f; b = 0.9569f;   // Ne
                                 break;
                        case 11: r = 0.6667f; g = 0.3686f; b = 0.9490f;   // Na
                                 break;
                        case 12: r = 0.5373f; g = 1.0000f; b = 0.0000f;   // Mg
                                 break; 
                        case 13: r = 1.0000f; g = 0.3922f; b = 1.0000f;   // Al
                                 break;
                        case 14: r = 1.0000f; g = 0.7843f; b = 0.1569f;   // Si
                                 break;
                        case 15: r = 0.9137f; g = 0.6118f; b = 1.0000f;   // P
                                 break;
                        case 16: r = 1.0000f; g = 0.9216f; b = 0.0000f;   // S
                                 break;
                        case 17: r = 0.7020f; g = 1.0000f; b = 0.6784f;   // Cl
                                 break;
                        case 18: r = 0.5059f; g = 0.8196f; b = 0.8941f;   // Ar
                                 break;
                        case 19: r = 0.5608f; g = 0.2549f; b = 0.8275f;   // K
                                 break;
                        case 20: r = 0.2392f; g = 1.0000f; b = 0.0000f;   // Ca
                                 break;
                        case 21: r = 0.9020f; g = 0.9020f; b = 0.8941f;   // Sc
                                 break;
                        case 22: r = 0.7529f; g = 0.7647f; b = 0.7765f;   // Ti
                                 break;
                        case 23: r = 0.6549f; g = 0.6471f; b = 0.6745f;   // V
                                 break;
                        case 24: r = 0.5451f; g = 0.6000f; b = 0.7765f;   // Cr
                                 break;
                        case 25: r = 0.6118f; g = 0.4824f; b = 0.7765f;   // Mn
                                 break;
                        case 26: r = 0.5059f; g = 0.4824f; b = 0.7765f;   // Fe
                                 break;
                        case 27: r = 0.3647f; g = 0.4275f; b = 1.0000f;   // Co
                                 break;
                        case 28: r = 0.3647f; g = 0.4824f; b = 0.7647f;   // Ni
                                 break;
                        case 29: r = 1.0000f; g = 0.4824f; b = 0.3843f;   // Cu
                                 break;
                        case 30: r = 0.4863f; g = 0.5059f; b = 0.6863f;   // Zn
                                 break;
                        case 31: r = 0.7647f; g = 0.5725f; b = 0.5686f;   // Ga
                                 break;
                        case 32: r = 0.4000f; g = 0.6784f; b = 0.3832f;   // Ge
                                 break;
                        case 33: r = 0.7451f; g = 0.5059f; b = 0.8902f;   // As
                                 break;
                        case 34: r = 1.0000f; g = 0.6353f; b = 0.0000f;   // Se
                                 break;
                        case 35: r = 0.8275f; g = 0.3098f; b = 0.2706f;   // Br
                                 break;
                        case 36: r = 0.3647f; g = 0.7294f; b = 0.8196f;   // Kr
                                 break;
                        case 37: r = 0.4431f; g = 0.1804f; b = 0.6980f;   // Rb
                                 break;
                        case 38: r = 0.0000f; g = 1.0000f; b = 0.0000f;   // Sr
                                 break;
                        case 39: r = 0.5882f; g = 0.9922f; b = 1.000f;    // Y
                                 break;
                        case 40: r = 0.5882f; g = 0.8824f; b = 0.8824f;   // Zr
                                 break;
                        case 41: r = 0.4549f; g = 0.7647f; b = 0.7961f;   // Nb
                                 break;
                        case 42: r = 0.3333f; g = 0.7098f; b = 0.7176f;   // Mo
                                 break;
                        case 43: r = 0.2353f; g = 0.6235f; b = 0.6588f;   // Tc
                                 break; 
                        case 44: r = 0.1373f; g = 0.5569f; b = 0.5922f;   // Ru
                                 break;
                        case 45: r = 0.0431f; g = 0.4863f; b = 0.5490f;   // Rh
                                 break;
                        case 46: r = 0.0000f; g = 0.4118f; b = 0.5255f;   // Pd
                                 break;
                        case 47: r = 0.6000f; g = 0.7765f; b = 1.0000f;   // Ag
                                 break;
                        case 48: r = 1.0000f; g = 0.8471f; b = 0.5686f;   // Cd
                                 break;
                        case 49: r = 0.6549f; g = 0.4627f; b = 0.4510f;   // In
                                 break;
                        case 50: r = 0.4000f; g = 0.5059f; b = 0.5059f;   // Sn
                                 break;
                        case 51: r = 0.6235f; g = 0.3961f; b = 0.7098f;   // Sb
                                 break;
                        case 52: r = 0.8353f; g = 0.4824f; b = 0.0000f;   // Te
                                 break;
                        case 53: r = 0.6667f; g = 0.3098f; b = 0.2706f;   // I
                                 break;
                        case 54: r = 0.2588f; g = 0.6235f; b = 0.6902f;   // Xe
                                 break;
                        case 55: r = 0.3412f; g = 0.0980f; b = 0.5608f;   // Cs
                                 break; 
                        case 56: r = 0.0000f; g = 0.7961f; b = 0.0000f;   // Ba
                                 break;
                        case 57: r = 0.4392f; g = 0.8706f; b = 1.0000f;   // La
                                 break;
                        case 58: r = 1.0000f; g = 1.0000f; b = 0.7843f;   // Ce
                                 break;
                        case 59: r = 0.8510f; g = 1.0000f; b = 0.7843f;   // Pr
                                 break;
                        case 60: r = 0.7412f; g = 1.0000f; b = 0.7843f;   // Nd
                                 break;
                        case 61: r = 0.6431f; g = 1.0000f; b = 0.7843f;   // Pm
                                 break;
                        case 62: r = 0.5725f; g = 1.0000f; b = 0.7843f;   // Sm
                                 break;
                        case 63: r = 0.3882f; g = 1.0000f; b = 0.7843f;   // Eu
                                 break;
                        case 64: r = 0.2784f; g = 1.0000f; b = 0.7843f;   // Gd
                                 break;
                        case 65: r = 0.1961f; g = 1.0000f; b = 0.7843f;   // Tb
                                 break;
                        case 66: r = 0.1216f; g = 1.0000f; b = 0.7176f;   // Dy
                                 break;
                        case 67: r = 0.0000f; g = 1.0000f; b = 0.6157f;   // Ho
                                 break;
                        case 68: r = 0.0000f; g = 0.9059f; b = 0.4627f;   // Er
                                 break;
                        case 69: r = 0.0000f; g = 0.8275f; b = 0.3255f;   // Tm
                                 break;
                        case 70: r = 0.0000f; g = 0.7529f; b = 0.2235f;   // Yb
                                 break;
                        case 71: r = 0.0000f; g = 0.6784f; b = 0.1373f;   // Lu
                                 break;
                        case 72: r = 0.3020f; g = 0.7608f; b = 1.0000f;   // Hf
                                 break;
                        case 73: r = 0.3020f; g = 0.6549f; b = 1.0000f;   // Ta
                                 break;
                        case 74: r = 0.1529f; g = 0.5804f; b = 0.8392f;   // W
                                 break;
                        case 75: r = 0.1529f; g = 0.4941f; b = 0.6745f;   // Re
                                 break;
                        case 76: r = 0.1529f; g = 0.4078f; b = 0.5922f;   // Os
                                 break;
                        case 77: r = 0.0941f; g = 0.3333f; b = 0.5294f;   // Ir
                                 break;
                        case 78: r = 0.0941f; g = 0.3569f; b = 0.5686f;   // Pt
                                 break;
                        case 79: r = 1.0000f; g = 0.8196f; b = 0.1412f;   // Au
                                 break;
                        case 80: r = 0.7098f; g = 0.7098f; b = 0.7647f;   // Hg
                                 break;
                        case 81: r = 0.6549f; g = 0.3333f; b = 0.3020f;   // Tl
                                 break;
                        case 82: r = 0.3412f; g = 0.3529f; b = 0.3765f;   // Pb
                                 break;
                        case 83: r = 0.6235f; g = 0.3098f; b = 0.7098f;   // Bi
                                 break;
                        case 84: r = 0.6745f; g = 0.3647f; b = 0.0000f;   // Po
                                 break;
                        case 85: r = 0.4627f; g = 0.3098f; b = 0.2706f;   // At
                                 break;
                        case 86: r = 0.2588f; g = 0.5176f; b = 0.5922f;   // Rn
                                 break;
                        case 87: r = 0.2588f; g = 0.0000f; b = 0.4000f;   // Fr
                                 break;
                        case 88: r = 0.0000f; g = 0.4863f; b = 0.0000f;   // Ra
                                 break;
                        case 89: r = 0.4431f; g = 0.6667f; b = 0.9882f;   // Ac
                                 break;
                        case 90: r = 0.0000f; g = 0.7333f; b = 1.0000f;   // Th
                                 break;
                        case 91: r = 0.0000f; g = 0.6314f; b = 1.0000f;   // Pa
                                 break;
                        case 92: r = 0.0000f; g = 0.5725f; b = 1.0000f;   // U
                                 break;
                        case 93: r = 0.0000f; g = 0.5059f; b = 0.9490f;   // Np
                                 break;
                        case 94: r = 0.0000f; g = 0.4196f; b = 0.9490f;   // Pu
                                 break;
                        case 95: r = 0.3333f; g = 0.3569f; b = 0.9490f;   // Am
                                 break;
                        case 96: r = 0.4706f; g = 0.3569f; b = 0.8902f;   // Cm
                                 break;
                        case 97: r = 0.5373f; g = 0.3098f; b = 0.8902f;   // Bk
                                 break;
                        case 98: r = 0.6314f; g = 0.2157f; b = 0.8353f;   // Cf
                                 break;
                        case 99: r = 0.7020f; g = 0.1216f; b = 0.8353f;   // Es
                                 break;
                       case 100: r = 0.7020f; g = 0.1216f; b = 0.7294f;   // Fm
                                 break;
                       case 101: r = 0.7020f; g = 0.0510f; b = 0.6549f;   // Md
                                 break;
                       case 102: r = 0.7412f; g = 0.0510f; b = 0.5294f;   // No
                                 break;
                       case 103: r = 0.7882f; g = 0.0000f; b = 0.4000f;   // Lr
                                 break;
                       case 104: r = 0.1961f; g = 0.5098f; b = 1.0000f;   // Rf
                                 break;
                       case 105: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Db
                                 break;
                       case 106: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Sg
                                 break;
                       case 107: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Bh
                                 break;
                       case 108: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Hs
                                 break;
                       case 109: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Mt
                                 break;
                       case 110: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Ds
                                 break;
                       case 111: r = 0.1569f; g = 0.5098f; b = 0.9020f;   // Rg
                                 break;
                     }

                     colors[3*j] = r;
                     colors[(3*j) + 1] = g;
                     colors[(3*j) + 2] = b;

                     atomRGB[3*j] = r;
                     atomRGB[(3*j) + 1] = g;
                     atomRGB[(3*j) + 2] = b;

               }    

               return colors;
          }

          public void display(GLDrawable gldraw)
          {
               boolean viewMolStructure;

               gl = gldraw.getGL();
               glu = gldraw.getGLU();

               GLUT glut = new GLUT(); 

               // Set up camera
               gl.glMatrixMode(GL.GL_PROJECTION);
               gl.glLoadIdentity();

               // Aspect ratio
               panelHeight = ((Component)gldraw).getHeight();
               panelWidth = ((Component)gldraw).getWidth();

               aspect = panelWidth/panelHeight;

               fov = 45.0;

               near = 0.001;
               far = 1000.0;

//               glu.gluPerspective(fov, aspect, near, far);
               gl.glOrtho(-panelWidth/100.0, panelWidth/100.0, -panelHeight/100.0, panelHeight/100.0, -far, far);

               gl.glMatrixMode(GL.GL_MODELVIEW);
               gl.glLoadIdentity();

               // Set background color of canvas
               gl.glClearColor(bkg_red, bkg_green, bkg_blue, color_alphaValue);
 
               // Define camera
               glu.gluLookAt(0, 0, 0, 0, 0, 20, 0, 1, 0);
    
               // Set up light(s)
               float position1[] = {25.0f, 25.0f, -50.0f, 0.0f};
               float position2[] = {25.0f, -25.0f, 0.0f, 0.0f};
               float position3[] = {-25.0f, 25.0f, 0.0f, 0.0f};

               float diffuse[] = {0.70f, 0.70f, 0.70f, 0.5f};
               float ambient[] = {0.275f, 0.275f, 0.275f, 0.25f};
               float specular[] = {1.0f, 1.0f, 1.0f, 1.0f};

               gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position1);
//               gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position2);
//               gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position3);
               gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse);
               gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient);
               gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular);

               gl.glEnable(GL.GL_LIGHTING);
               gl.glEnable(GL.GL_LIGHT0);
               gl.glEnable(GL.GL_DEPTH_TEST);

               gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

               // Draw 3D coordinate axes
               float[] x_axismat = {0.7f, 0.2f, 0.2f, 1.0f};
               float[] y_axismat = {0.2f, 0.7f, 0.2f, 1.0f};
               float[] z_axismat = {0.2f, 0.2f, 0.7f, 1.0f};

               gl.glLineWidth(2.0f);
               
               gl.glPushMatrix();

               gl.glTranslated(-7.2, -4.0, 0.0);

               gl.glRotated(theta, 0.0, 1.0, 0.0);            // rotate about y-axis 
               gl.glRotated(phi, 0.0, 0.0, 1.0);              // rotate about z-axis
               gl.glRotated(psi, 1.0, 0.0, 0.0);              // rotate about x-axis   

               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, x_axismat);
               gl.glBegin(GL.GL_LINES);
               gl.glVertex3d(0.0, 0.0, 0.0);
               gl.glVertex3d(0.65, 0.0, 0.0);
               gl.glEnd();

               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, y_axismat);
               gl.glBegin(GL.GL_LINES);
               gl.glVertex3d(0.0, 0.0, 0.0);
               gl.glVertex3d(0.0, 0.65, 0.0);
               gl.glEnd();

               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, z_axismat);
               gl.glBegin(GL.GL_LINES);
               gl.glVertex3d(0.0, 0.0, 0.0);
               gl.glVertex3d(0.0, 0.0, 0.65);
               gl.glEnd();

               gl.glRotated(-theta, 0.0, 1.0, 0.0);            // rotate about y-axis 
               gl.glRotated(-phi, 0.0, 0.0, 1.0);              // rotate about z-axis
               gl.glRotated(-psi, 1.0, 0.0, 0.0);              // rotate about x-axis  

               gl.glTranslated(7.2, 4.0, 0.0);

               gl.glPopMatrix();

               float rgb[] = {0.9f, 0.0f, 0.0f, 0.0f, 0.9f, 0.3f, 0.0f, 0.0f, 0.9f};  

               selectMode = getSelectionMode();

               gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

               // Draw center cross hairs 
               drawStructure(gldraw, atomCoordinates, atomConnectivity, elementRGB, atomRadii, bondVectors, theta, phi, natoms, selectMode, selectedNames, molScaleFactor, canvasScaleFactor);
               displayStructure = false;

               if (displayStructure == true)
               {
                    drawStructure(gldraw, atomCoordinates, atomConnectivity, elementRGB, atomRadii, bondVectors, theta, phi, natoms, selectMode, selectedNames, molScaleFactor, canvasScaleFactor);
               }

               if (importStructureFile == true)
               { 
//                    drawStructure(gldraw, atomCoordinates, atomConnectivity, theta, phi, natoms, mode);
               }
          }

          public int getSelectionMode()
          {
               return selectMode;
          }

          public String getDisplayStyle()
          {
               return displayStyle;
          }

          public void drawStructure(GLDrawable gldraw, double[][] atomCoordinates, int[][] atomConnectivity, float[][] elementRGB, ArrayList<Double> atomRadii, double[][] bondVectors, double theta, double phi, int natoms, int selectMode, ArrayList<Integer> selectedNames, double molScaleFactor, double canvasScaleFactor)
          {
               gl = gldraw.getGL();
               glu = gldraw.getGLU();

               GLUquadric quadric = glu.gluNewQuadric();

               int connectivity, bondname, bondOrdr;
               int ndx, ndx2, ndx3, ndexx, pointNdx;

               double dx12, dy12, dz12, dx23, dy23, dz23, d23, atmRadius, bd, r, r1, r2;
               double angle, rot_x, rot_y, rot_z;                                        
               double nx, ny, nz, nbvx, nbvy, nbvz;
               double n11, n22, n33;
               double rx12, ry12, rz12, rx13, ry13, rz13, bvx, bvy, bvz, Dbv, K, Kbr;

               double nx1, ny1, nz1;    // components of vector normal to plane containing double bond 
               double n1, n2, n3;       // components of vector parallel to plane containing double 
                                        // bond and normal to double bond

               double tx, ty, tz, xdel, ydel, zdel;
               double eps, psf;
               double vx1, vx2, vy1, vy2, vz1, vz2;
               double vxstar, vystar, vzstar;

               float[] atomMat = new float[4];
               float[] bondMat = new float[4];
               float[] bvColor = new float[4];

               String X, Y, Z;

               mouseButtonPressed = getMouseButtonPressed();
               xyzKeyDown = getXYZKeyDown();

               draggedMouse = getMouseDraggedState();

               bondRadius = 0.05; 
               atomRad = 0.20;

               bondsColored = false;


boolean globalRotation = false;
globalRotation = getGlobalAxisRotationStatus();

globalRotationAxes = getGlobalRotationAxes();
nxx = globalRotationAxes[0];  nyy = globalRotationAxes[1];  nzz = globalRotationAxes[2];  
//nxx = nyy = nzz = 1.0;

               // Draw 3D coordinate axes
               float[] x_axismat = {0.7f, 0.2f, 0.2f, 1.0f};
               float[] y_axismat = {0.2f, 0.7f, 0.2f, 1.0f};
               float[] z_axismat = {1.0f, 1.0f, 0.6f, 1.0f};

               gl.glPushMatrix();

               delCoords = getDelCoords();

               gl.glTranslated(-0.425*panelWidth*ang2pixls, -0.425*panelHeight*ang2pixls, 0.0);


               gl.glRotated(theta, 0.0, 1.0, 0.0);            // rotate about y-axis 
               gl.glRotated(phi, 0.0, 0.0, 1.0);              // rotate about x-axis
               gl.glRotated(psi, 1.0, 0.0, 0.0);              // rotate about z-axis   
               gl.glRotated(180, 0.0, 1.0, 0.0);              // rotate 180 degrees about y-axis


//               gl.glRotated(180, 0.0, nyy, 0.0);              // rotate 180 degrees about y-axis
//               gl.glRotated(theta, 0.0, nyy, 0.0);            // rotate about y-axis 
//               gl.glRotated(phi, 0.0, 0.0, nzz);              // rotate about z-axis
//               gl.glRotated(psi, nxx, 0.0, 0.0);              // rotate about x-axis   



//               gl.glRotated(psi, nxx, 0.0, 0.0);              // rotate about x-axis   
//               gl.glRotated(theta, 0.0, nyy, 0.0);            // rotate about y-axis 
//               gl.glRotated(phi, 0.0, 0.0, nzz);              // rotate about z-axis


               gl.glPushMatrix();
               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, z_axismat);
               glu.gluCylinder(quadric, 0.02, 0.02, 0.6, 20, 20);
               gl.glRotated(90, 0.0, 1.0, 0.0); 
               gl.glPopMatrix();

               gl.glPushMatrix();
               gl.glRotated(90, 0.0, 1.0, 0.0); 
               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, x_axismat);
               glu.gluCylinder(quadric, 0.02, 0.02, 0.6, 20, 20);
               gl.glPopMatrix();

               gl.glPushMatrix();
               gl.glRotated(-90, 1.0, 0.0, 0.0);
               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, y_axismat);
               glu.gluCylinder(quadric, 0.02, 0.02, 0.6, 20, 20);
               gl.glRotated(90, 1.0, 0.0, 0.0); 
               gl.glPopMatrix();
               // End draw 3D coordinate axes

               gl.glPopMatrix();

               if (natoms == 0)
               {
                    float[] c_axismat = {1.0f, 1.0f, 1.0f, 1.0f};

                    gl.glLineWidth(1.2f);

                    gl.glTranslated(0.0, 0.0, 0.0);
//                    gl.glTranslated(x0, y0, (z0 + (zloc + 7.0)));

                    vx1 = -0.15; vy1 = 0.0; vz1 = 0.0;
                    vx2 = 0.15; vy2 = 0.0; vz2 = 0.0;
  
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, c_axismat);

                    gl.glPushMatrix(); 
                       
                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3d(vx1, vy1, vz1);
                    gl.glVertex3d(vx2, vy2, vz2);
                    gl.glEnd();

                    gl.glPopMatrix(); 

                    vx1 = 0.0; vy1 = -0.15; vz1 = 0.0;
                    vx2 = 0.0; vy2 = 0.15; vz2 = 0.0;

                    gl.glPushMatrix();

                    gl.glBegin(GL.GL_LINES);
                    gl.glVertex3d(vx1, vy1, vz1);
                    gl.glVertex3d(vx2, vy2, vz2);
                    gl.glEnd();

                    gl.glPopMatrix();
               }

               gl.glTranslated(x0, y0, (z0 + (zloc + 7.0)));



//double nxx, nyy, nzz;
//nxx = nyy = nzz = 1.0;

//rotationAxisID = getRotationAxisID();

//gl.glRotated(180, 0.0, 1.0, 0.0);


/*
               gl.glRotated(180, nyy1, nyy2, nyy3);

if (rotAxis.equals("x"))
{
//               gl.glRotated(180, nxx1, nxx2, nxx3);
     gl.glRotated(-psi, nxx1, nxx2, nxx3); 
     gl.glRotated(theta, nyy1, nyy2, nyy3);
     gl.glRotated(-phi, nzz1, nzz2, nzz3); 
}
else if (rotAxis.equals("y"))
{
//               gl.glRotated(180, nyy1, nyy2, nyy3);
     gl.glRotated(theta, nyy1, nyy2, nyy3);
     gl.glRotated(-phi, nzz1, nzz2, nzz3); 
     gl.glRotated(-psi, nxx1, nxx2, nxx3); 
}
else if (rotAxis.equals("z"))
{
//               gl.glRotated(180, nzz1, nzz2, nzz3);
     gl.glRotated(-phi, nzz1, nzz2, nzz3); 
     gl.glRotated(-psi, nxx1, nxx2, nxx3);
//               gl.glRotated(180, nyy1, nyy2, nyy3);
     gl.glRotated(theta, nyy1, nyy2, nyy3); 
}
*/


               gl.glRotated(180, nyy1, nyy2, nyy3);
               gl.glRotated(theta, nyy1, nyy2, nyy3);             // rotate about y-axis 
               gl.glRotated(-phi, nzz1, nzz2, nzz3);              // rotate about z-axis
               gl.glRotated(-psi, nxx1, nxx2, nxx3);              // rotate about x-axis 



//               gl.glRotated(180, 0.0, nyy, 0.0);  
//               gl.glRotated(180, 0.0, 1.0, 0.0);
//               gl.glRotated(theta, 0.0, nyy, 0.0);             // rotate about y-axis 
//               gl.glRotated(-phi, 0.0, 0.0, nzz);              // rotate about z-axis
//               gl.glRotated(-psi, nxx, 0.0, 0.0);              // rotate about x-axis  



//               gl.glRotated(180, 0.0, 1.0, 0.0);  


/*
  if (rotationAxisID.equals("x"))
  {
//       gl.glRotated(180, 1.0, 0.0, 0.0);

       gl.glRotated(-psi, 1.0, 0.0, 0.0);
       gl.glRotated(theta0, 0.0, 1.0, 0.0);
       gl.glRotated(-phi0, 0.0, 0.0, 1.0);

//       gl.glRotated(theta0, 0.0, 1.0, 0.0);
//       gl.glRotated(-phi0, 0.0, 0.0, 1.0);
//       gl.glRotated(-psi, 1.0, 0.0, 0.0);


  }
  else if (rotationAxisID.equals("y"))
  {

       gl.glRotated(theta, 0.0, 1.0, 0.0);
       gl.glRotated(-phi0, 0.0, 0.0, 1.0);
       gl.glRotated(-psi0, 1.0, 0.0, 0.0);


  }
  else if (rotationAxisID.equals("z"))
  {

       gl.glRotated(-phi, 0.0, 0.0, 1.0);
       gl.glRotated(theta0, 0.0, 1.0, 0.0);
       gl.glRotated(-psi0, 1.0, 0.0, 0.0);

//       gl.glRotated(theta0, 0.0, 1.0, 0.0);
//       gl.glRotated(-phi, 0.0, 0.0, 1.0);
//       gl.glRotated(-psi, 1.0, 0.0, 0.0);

  }
  else
  {

//               gl.glRotated(theta, 0.0, nyy, 0.0);             // rotate about y-axis 
//               gl.glRotated(-phi, 0.0, 0.0, nzz);              // rotate about z-axis
//               gl.glRotated(-psi, nxx, 0.0, 0.0);              // rotate about x-axis  

               gl.glRotated(theta, 0.0, 1.0, 0.0);             // rotate about y-axis 
               gl.glRotated(-phi, 0.0, 0.0, 1.0);              // rotate about z-axis
               gl.glRotated(-psi, 1.0, 0.0, 0.0);              // rotate about x-axis  
  }
*/


               psf = 1.0;

               // Scale molecule to fit canvas
               combScaleFactor = molScaleFactor * canvasScaleFactor;
               gl.glScaled(combScaleFactor, combScaleFactor, combScaleFactor);

               // Center molecule in display panel
               gl.glTranslated(-delCoords[0], -delCoords[1], -delCoords[2]);

               // If a fragment has been selected, get fragment atoms when mouse pressed in blank area of canvas
               fragmentSelected = getFragmentSelectedStatus();

               if ((selectedNames.size() <= 0) && (fragmentSelected))
               {
                    selectedFragmentAtoms = getSelectedFragmentAtoms();

                    for (j = 0; j < selectedFragmentAtoms.size(); j++)
                    {
                         selectedNames.add(selectedFragmentAtoms.get(j));
                    }
               }

               // Draw atoms
               for (j = 0; j < natoms; j++)
               {
                    gl.glPushMatrix();

                    gl.glTranslated(psf * atomCoordinates[j][1], psf * atomCoordinates[j][2], psf * atomCoordinates[j][3]); 

                    // Recolor all selected atoms

                    if ( (selectedNames.size() > 0) && (selectedNames.contains(j)) )
                    {
                         // Unmask selected fragment atoms as mouse is dragged
                         if (draggedMouse)
                         {
                              for (k = 0; k < ncolors; k++)
                              {
                                   atomMat[k] = elementRGB[j][k];
                              }
                         }
                         else
                         {
                              atomMat[0] = 1.0f; atomMat[1] = 1.0f; atomMat[2] = 0.4f; 
                         }         
                    }
                    else
                    {
                         for (k = 0; k < ncolors; k++)
                         {
                              atomMat[k] = elementRGB[j][k];
                         }
                    }

                    atomMat[3] = material_alphaValue; 

                    displayStyle = getDisplayStyle();

                    atmRadius = atomRadii.get(j);

                    if (displayStyle.equals("line"))
                    {
                         bondRadius = 0.03;

                         if ( (selectedNames.size() == 1) && (selectedNames.contains(j)) )
                         {
                              atmRadius = 2.5*bondRadius;
                         }
                         else
                         {
                              atmRadius = bondRadius;
                         }
 
                         bondsColored = true;
                    }

                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, atomMat);

                    glut.glutSolidSphere(glu, atmRadius, 100, 360); 

                    gl.glPopMatrix();
               }

               // Draw bonds

               double discRadius = 10*bondRadius;
               double discHeight = 0.125;

               for (j = 0; j < natoms; j++)
               {
                    for (k = (j+1); k < natoms; k++)
                    {
                         if (atomConnectivity[j][k] != 0)
                         {
                              gl.glPushMatrix();

                              gl.glTranslated(psf * atomCoordinates[k][1], psf * atomCoordinates[k][2], psf * atomCoordinates[k][3]); 

                              dx12 = (atomCoordinates[j][1] - atomCoordinates[k][1]);
                              dy12 = (atomCoordinates[j][2] - atomCoordinates[k][2]);
                              dz12 = (atomCoordinates[j][3] - atomCoordinates[k][3]);

                              bd = psf * Math.sqrt(dx12*dx12 + dy12*dy12 + dz12*dz12);
 
                              r = Math.sqrt(dx12*dx12 + dy12*dy12);

                              if (r > 0.0)
                              {
                                   n11 = -dy12/r;
                                   n22 = dx12/r;
                                   n33 = 0.0;

                                   if (dz12 >= 0.0)
                                   {
                                        angle = (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                                   }
                                   else     
                                   {
                                        angle = 180.0 - (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                                   }
                              }
                              else
                              {
                                   n11 = 0.0;
                                   n22 = 0.0;
                                   n33 = 1.0;

                                   if (dz12 >= 0.0)  
                                   {
                                        angle = 0.0;
                                   }
                                   else     
                                   {
                                        angle = 180.0;
                                   }
                              }
 
                              if (bondsColored == false)
                              {
                                   gl.glRotated(angle, n11, n22, n33);

                                   material_alphaValue = 1.0f;
 
                                   bondname = (1000000*(j+1)) + k;
 
                                   // Recolor all selected bonds
                                   if ( (selectedNames.size() > 0) && (selectedNames.contains(bondname)) )
                                   {
                                        bondMat[0] = 1.0f; bondMat[1] = 1.0f; bondMat[2] = 0.0f; 
                                   }
                                   else
                                   {
                                        bondMat[0] = 1.000f; bondMat[1] = 1.000f; bondMat[2] = 1.000f;
                                   }

                                   bondMat[3] = material_alphaValue; 
 
                                   gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat); 
  
                                   if (atomConnectivity[j][k] == 1)
                                   {
                                        glu.gluCylinder(quadric, bondRadius, bondRadius, bd, 20, 20); 

                                        if ( (nrowsSelected > 0) || (idCentralDhBond) )
                                        {
                                             for (ndexx = 0; ndexx < minDHCentralBondIndices.size(); ndexx++)
                                             {
                                                  if ( (j == minDHCentralBondIndices.get(ndexx)) && (k == maxDHCentralBondIndices.get(ndexx)) )
                                                  {
                                                       gl.glRotated(-angle, n11, n22, n33);
                                                       gl.glTranslated(dx12/2, dy12/2, dz12/2); 
                                                       gl.glRotated(angle, n11, n22, n33);

                                                       bondMat[0] = 1.0f; bondMat[1] = 1.0f; bondMat[2] = 0.0f;
                                                       gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat); 

                                                       for (int ncylndrs = 0; ncylndrs < 10; ncylndrs++)
                                                       {
                                                            glu.gluCylinder(quadric, discRadius-0.01*ncylndrs, discRadius-0.01*ncylndrs, discHeight, 20, 20);
                                                       }
                                                  }  
                                             }
                                        }
                                   }
                                   else if ( (atomConnectivity[j][k] == 2) || (atomConnectivity[j][k] == 3) )
                                   {
                                             ndx3 = -1;

                                             for (ndx = 0; ndx < natoms; ndx++)
                                             {
                                                  if ( (atomConnectivity[j][ndx] > 0) && (ndx != k) )
                                                  {
                                                       ndx3 = ndx;
                                                       break;
                                                  }
                                             }

                                             if (ndx3 < 0)
                                             {
                                                  for (ndx = 0; ndx < natoms; ndx++)
                                                  {
                                                       if ( (atomConnectivity[k][ndx] > 0) && (ndx != j) )
                                                       {
                                                            ndx3 = ndx;
                                                            break;
                                                       }
                                                       else
                                                       {
                                                            ndx3 = 0;
                                                       }
                                                  }
                                             } 

                                             // Components of vector along double bond
                                             rx12 = (-dx12/bd); ry12 = (-dy12/bd); rz12 = (-dz12/bd); 

                                             // If first atom drawn has sp2 hybridization
                                             if (natoms == 2)
                                             {
                                                  dx23 = (atomCoordinates[0][1] - atomCoordinates[1][1]);
                                                  dy23 = (atomCoordinates[0][2] - atomCoordinates[1][2]);
                                                  dz23 = (atomCoordinates[0][3] - atomCoordinates[1][3]);
                                             }
                                             else
                                             {
                                                  // Compute distance between one double bonded atom and coplanar single bonded atom
                                                  dx23 = (atomCoordinates[ndx3][1] - atomCoordinates[j][1]);
                                                  dy23 = (atomCoordinates[ndx3][2] - atomCoordinates[j][2]);
                                                  dz23 = (atomCoordinates[ndx3][3] - atomCoordinates[j][3]);
                                             }

                                             d23 = Math.sqrt(dx23*dx23 + dy23*dy23 + dz23*dz23);

                                             // Components of vector along coplanar atom pairs
                                             rx13 = dx23/d23; ry13 = dy23/d23; rz13 = dz23/d23; 

                                             if (natoms == 2)
                                             {
                                                  rx13 = ry13 = rz13 = 0.0;

                                                  if (bondVectors.length > 0)
                                                  {
                                                       for (indx = 0; indx < bondVectors.length; indx++)
                                                       {                                                      
                                                            int BVindx = ( -((int)(bondVectors[indx][0]/100) + 1) );

                                                            if ( (BVindx == 0) || (BVindx == 1) )
                                                            {
                                                                 if ( (int)(bondVectors[indx][4]) == 1)
                                                                 {
                                                                      rx13 = bondVectors[indx][1];  ry13 = bondVectors[indx][2];  rz13 = bondVectors[indx][3];  
                                                                      break;
                                                                 }
                                                            }
                                                       }
                                                  }
                                             }

                                             // Compute unit vector normal to plane of the three coplanar atoms
                                             nx1 = (ry12*rz13 - ry13*rz12);
                                             ny1 = (rx13*rz12 - rx12*rz13);
                                             nz1 = (rx12*ry13 - rx13*ry12);

                                             if ( (nx1 == 0.0) && (ny1 == 0.0) && (nz1 == 0.0) )
                                             {
                                                  n1 = 1.0;  n2 = n3 = 0.0;

                                                  if (dz23 != 1.0)
                                                  {
                                                       n1 = dy23;  n2 = -dx23;  n3 = 0.0;
                                                  }
                                             }
                                             else
                                             { 
                                                  r = Math.sqrt((nx1*nx1) + (ny1*ny1) + (nz1*nz1));
 
                                                  nx1 = nx1/r;  ny1 = ny1/r;  nz1 = nz1/r;

                                                  // Compute unit vector parallel to plane containing double bond and normal to double bond
                                                  n1 = (ny1*rz12 - nz1*ry12);
                                                  n2 = (nz1*rx12 - nx1*rz12);
                                                  n3 = (nx1*ry12 - ny1*rx12);
                                             }

                                             r = Math.sqrt((n1*n1) + (n2*n2) + (n3*n3));
 
                                             n1 = n1/r;  n2 = n2/r;  n3 = n3/r;

                                             //eps = 1.75;
                                             eps = 1.50;
 
                                             double brp = 0.65;

                                             xdel = eps*(n1*bondRadius);
                                             ydel = eps*(n2*bondRadius);
                                             zdel = eps*(n3*bondRadius); 

                                             // Draw central sp bond if bond order is 3 (triple bond)
                                             if (atomConnectivity[j][k] == 3)
                                             {
                                                  glu.gluCylinder(quadric, brp*bondRadius, brp*bondRadius, bd, 20, 20);  
                                             }

                                             // Draw remaining bonds (sp2 and single pi bonds for double bond; two pi bonds for triple bond)
                                             gl.glTranslated(1.5*xdel, 1.5*ydel, 1.5*zdel);
                                             glu.gluCylinder(quadric, brp*bondRadius, brp*bondRadius, bd, 20, 20); 

                                             gl.glTranslated(-3*xdel, -3*ydel, -3*zdel);
                                             glu.gluCylinder(quadric, brp*bondRadius, brp*bondRadius, bd, 20, 20); 
                                             gl.glTranslated(1.5*xdel, 1.5*ydel, 1.5*zdel);

                                             if ( (nrowsSelected > 0) || (idCentralDhBond) )
                                             {
                                             for (ndexx = 0; ndexx < minDHCentralBondIndices.size(); ndexx++)
                                             {
                                                  if ( (j == minDHCentralBondIndices.get(ndexx)) && (k == maxDHCentralBondIndices.get(ndexx)) )
                                                  {
                                                       gl.glRotated(-angle, n11, n22, n33);
                                                       gl.glTranslated(dx12/2, dy12/2, dz12/2); 
                                                       gl.glRotated(angle, n11, n22, n33);

                                                       bondMat[0] = 1.0f; bondMat[1] = 1.0f; bondMat[2] = 0.0f;
                                                       gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat); 

                                                       for (int ncylndrs = 0; ncylndrs < 10; ncylndrs++)
                                                       {
                                                            glu.gluCylinder(quadric, discRadius-0.01*ncylndrs, discRadius-0.01*ncylndrs, discHeight, 20, 20);
                                                       }
                                                  }  
                                             }
                                        }
    
                                   }
                              }
                              else
                              {
                                   bondMat1[0] = elementRGB[k][0];              // atomColors[(3*k)];
                                   bondMat1[1] = elementRGB[k][1];              // atomColors[(3*k) + 1];
                                   bondMat1[2] = elementRGB[k][2];              // atomColors[(3*k) + 2];
                                   bondMat1[3] = material_alphaValue;

                                   bondMat2[0] = elementRGB[j][0];              // atomColors[(3*j)];
                                   bondMat2[1] = elementRGB[j][1];              // atomColors[(3*j) + 1];
                                   bondMat2[2] = elementRGB[j][2];              // atomColors[(3*j) + 2];
                                   bondMat2[3] = material_alphaValue;   

                                   gl.glRotated(angle, n11, n22, n33);
                                   gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat1); 
                                   glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);

                                   gl.glRotated(-angle, n11, n22, n33);
                                   gl.glTranslated(dx12/2, dy12/2, dz12/2);
                                   gl.glRotated(angle, n11, n22, n33);

                                   gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat2); 
                                   glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);


/////////////////

/*

if ( ((j == 0) && (k == 1)) || ((j == 3) && (k == 4)) )
{
  gl.glRotated(-angle, n11, n22, n33);
  gl.glTranslated(dx12/2, dy12/2, dz12/2); 
  gl.glRotated(angle, n11, n22, n33);

  bondMat[0] = 1.0f; bondMat[1] = 1.0f; bondMat[2] = 0.0f;
  gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat); 

  for (int ncylndrs = 0; ncylndrs < 10; ncylndrs++)
  {
       glu.gluCylinder(quadric, discRadius-0.01*ncylndrs, discRadius-0.01*ncylndrs, discHeight, 20, 20);
  }
}

*/

//////////////////


                              }

                              gl.glPopMatrix();
                          }
                    }
               }

               // Draw bond vectors
               if (bondVectors.length > 0)
               {
                    // Default width of bond vector for single bonds 
                    float defaultLineWidth = 3.5f;

                    nbvRows = bondVectors.length;

                    float red, green, blue;
                       red = 0.0f; green = 1.0f; blue = 0.8f; 
 
                    for (ndx = 0; ndx < nbvRows; ndx++)
                    {
                         atomIndex = ( -((int)(bondVectors[ndx][0]/100) + 1) );
                         bondOrdr = (int)bondVectors[ndx][4];

                         if ((int)bondVectors[ndx][5] == 1)
                         {
                              vx1 = atomCoordinates[atomIndex][1];  vx2 = bondVectors[ndx][1] + atomCoordinates[atomIndex][1];
                              vy1 = atomCoordinates[atomIndex][2];  vy2 = bondVectors[ndx][2] + atomCoordinates[atomIndex][2];                         
                              vz1 = atomCoordinates[atomIndex][3];  vz2 = bondVectors[ndx][3] + atomCoordinates[atomIndex][3];

                              beta = getBeta();

                              if ( (selectedNames.size() == 1) && (selectedNames.get(0) == 0) )
                              {
                                   gl.glTranslated(vx1, vy1, vz1);
 
                                   vxstar = (vx2-vx1)*Math.cos(beta) + (vy2-vy1)*Math.sin(beta);
                                   vystar = -(vx2-vx1)*Math.sin(beta) + (vy2-vy1)*Math.cos(beta); 
                                   vzstar = vz2-vz1;

                                   gl.glTranslated(-vx1, -vy1, -vz1);          
                              }

                              vxstar = 0.8*(vx2-vx1); vystar = 0.8*(vy2-vy1); vzstar = 0.8*(vz2-vz1);
                              vx2 = vx1 + vxstar; vy2 = vy1 + vystar; vz2 = vz1 + vzstar;
                              
                              nameSelected = getNameSelected();

                              if ( (int)bondVectors[ndx][0] == nameSelected )
                              {
                                 bvColor[0] = 0.7f; bvColor[1] = 0.0f; bvColor[2] = 0.0f; bvColor[3] = 1.0f; 
                              }
                              else if ( (int)bondVectors[ndx][4] == 2 )
                              {
                                 bvColor[0] = 1.0f; bvColor[1] = 0.8f; bvColor[2] = 1.0f; bvColor[3] = 1.0f;   
                              }
                              else if ( (int)bondVectors[ndx][4] == 3 )
                              {
                                 bvColor[0] = 1.0f; bvColor[1] = 1.0f; bvColor[2] = 0.0f; bvColor[3] = 1.0f;   
                              }
                              else
                              {
                                   bvColor[0] = red; bvColor[1] = green; bvColor[2] = blue; bvColor[3] = 1.0f;
                              }

                              gl.glLineWidth(defaultLineWidth);      // default bond vector line width

                              if (bondOrdr == 2)
                              {
                                   gl.glLineWidth((float)(0.7*defaultLineWidth));
                              }
                              else if (bondOrdr == 3)
                              {
                                   gl.glLineWidth((float)(0.75*defaultLineWidth));
                              }

                              // Color bonds red if selected with Cntrl key down (as part of bond angle or dihedral angle)
                              if ( (selectedNames.size() > 1) && (selectedNames.contains((int)bondVectors[ndx][0])) )
                              {
                                   bvColor[0] = 0.8f;  bvColor[1] = 0.0f;  bvColor[3] = 0.0f;  bvColor[3] = 1.0f;
                              }

                              gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bvColor);


                              nbvx = 1.0;  nbvy = nbvz = 0.0;

                              if (bondOrdr > 1)
                              {
                                   bvx = vx2 - vx1;  bvy = vy2-vy1;  bvz = vz2-vz1; 

                                   if (Math.abs(bvz) != 1.0)
                                   {
                                        Dbv = Math.sqrt((bvx*bvx) + (bvy*bvy));

                                        nbvx = bvy/Dbv;  nbvy = (-bvx)/Dbv;  nbvz = 0.0;
                                   }

                                   if (bondOrdr == 2)
                                   { 
                                        K = 1.5;
                                        Kbr = K*bondRadius;

                                        gl.glPushMatrix(); 

                                        gl.glTranslated(Kbr*nbvx, Kbr*nbvy, Kbr*nbvz);

                                        gl.glBegin(GL.GL_LINES);
                                        gl.glVertex3d(vx1, vy1, vz1);
                                        gl.glVertex3d(vx2, vy2, vz2);
                                        gl.glEnd();

                                        gl.glTranslated(vx2, vy2, vz2);
                              
                                        glut.glutSolidSphere(glu, 0.08, 100, 360);

                                        gl.glPopMatrix();

                                        gl.glPushMatrix(); 

                                        gl.glTranslated(-Kbr*nbvx, -Kbr*nbvy, -Kbr*nbvz);

                                        gl.glBegin(GL.GL_LINES);
                                        gl.glVertex3d(vx1, vy1, vz1);
                                        gl.glVertex3d(vx2, vy2, vz2);
                                        gl.glEnd();

                                        gl.glTranslated(vx2, vy2, vz2);
                              
                                        glut.glutSolidSphere(glu, 0.08, 100, 360);

                                        gl.glPopMatrix();
                                   }
                                   else if (bondOrdr == 3)
                                   { 
                                        double reducdBondRadius;

                                        K = 2.0;
                                        Kbr = K*bondRadius;

                                        reducdBondRadius = 0.75*bondRadius;

                                   gl.glPushMatrix(); 
                       
                                   gl.glBegin(GL.GL_LINES);
                                   gl.glVertex3d(vx1, vy1, vz1);
                                   gl.glVertex3d(vx2, vy2, vz2);
                                   gl.glEnd();

                                   gl.glTranslated(vx2, vy2, vz2);
                                
                                   glut.glutSolidSphere(glu, 0.08, 100, 360);

                                   gl.glPopMatrix();

                                        gl.glPushMatrix(); 

                                        gl.glTranslated(Kbr*nbvx, Kbr*nbvy, Kbr*nbvz);

                                        gl.glBegin(GL.GL_LINES);
                                        gl.glVertex3d(vx1, vy1, vz1);
                                        gl.glVertex3d(vx2, vy2, vz2);
                                        gl.glEnd();

                                        gl.glTranslated(vx2, vy2, vz2);
                              
                                        glut.glutSolidSphere(glu, 0.08, 100, 360);

                                        gl.glPopMatrix();

                                        gl.glPushMatrix(); 

                                        gl.glTranslated(-Kbr*nbvx, -Kbr*nbvy, -Kbr*nbvz);

                                        gl.glBegin(GL.GL_LINES);
                                        gl.glVertex3d(vx1, vy1, vz1);
                                        gl.glVertex3d(vx2, vy2, vz2);
                                        gl.glEnd();

                                        gl.glTranslated(vx2, vy2, vz2);
                              
                                        glut.glutSolidSphere(glu, 0.08, 100, 360);

                                        gl.glPopMatrix();
                                   }
                              }
                              else
                              {
                                   gl.glPushMatrix(); 
                       
                                   gl.glBegin(GL.GL_LINES);
                                   gl.glVertex3d(vx1, vy1, vz1);
                                   gl.glVertex3d(vx2, vy2, vz2);
                                   gl.glEnd();

                                   double a1, a2,a3, axialx, axialy, axialz, csrho, snrho, rho;

                                   a1 = vx2-vx1;  a2 = vy2-vy1;  a3 = vz2-vz1;

                                   axialx = (-a2);  axialy = a1;  axialz = 0.0;

                                   csrho = a3/(Math.sqrt( (a1*a1) + (a2*a2) + (a3*a3) ));
                                   snrho = Math.sqrt( 1 - (csrho*csrho) );

                                   angle = (180.0/Math.PI)*Math.acos(csrho);

                                   gl.glTranslated(vx1, vy1, vz1);

                                   gl.glRotated(angle, axialx, axialy, axialz);

                                   glu.gluCylinder(quadric, 0.05, 0.05, 0.80, 20, 20); 

                                   gl.glRotated(-angle, axialx, axialy, axialz);

                                   gl.glTranslated(-vx1, -vy1, -vz1);

                                   gl.glPopMatrix();
                              }
                         }
                    }
               }

               if (selectMode == 1)
               {
                    displaySelection(gldraw, atomCoordinates, atomConnectivity, elementRGB, atomRadii, bondVectors, theta, phi, natoms);  

                    nameSelected = getNameSelected();
               }
          }

          public void sketchMolecule(GLDrawable gldraw, double theta, double phi, ArrayList coords, boolean sketchBond) 
          {
               gl = gldraw.getGL();
               glu = gldraw.getGLU();

               GLUquadric quadric = glu.gluNewQuadric();

               gl.glPushMatrix();

               gl.glTranslated(x0, y0, (z0 + zloc));

               gl.glTranslated(0.0, 0.0, 7.0 + zloc);

               gl.glRotated(theta, 0.0, 1.0, 0.0);
               gl.glRotated(phi, 0.0, 0.0, 1.0);
//               gl.glTranslated(x0, y0, z0); 

               red = 1.0f;
               green = 1.0f;
               blue = 1.0f;

               material_alphaValue = 1.0f;

               material = new float[4];

               material[0] = red;
               material[1] = green;
               material[2] = blue;
               material[3] = material_alphaValue; 

               radius = 0.35;

               k = (natomsSketched - 1);

               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, material);

               gl.glPushMatrix();
       
               glut.glutSolidSphere(glu, radius, 100, 360);

               gl.glTranslated(x0, y0, (z0 + zloc));

               gl.glTranslated(0.0, 0.0, 7.0 + zloc);

               gl.glRotated(theta, 0.0, 1.0, 0.0);
               gl.glRotated(phi, 0.0, 0.0, 1.0);
//               gl.glTranslated(x0, y0, z0); 

               red = 1.0f;
               green = 0.0f;
               blue = 0.0f;

               material_alphaValue = 1.0f;

               material = new float[4];

               material[0] = red;
               material[1] = green;
               material[2] = blue;
               material[3] = material_alphaValue;

               radius = 0.25;

               k = (natomsSketched - 1);

               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, material);

               gl.glPushMatrix();

               glut.glutSolidSphere(glu, radius, 100, 360);

               gl.glPopMatrix();
          }

          public void drawMolecule(GLDrawable gldrawable, double theta, double phi, double[] coords, boolean displayGeom, boolean bondsVisible, boolean bondsColored, int scaleRadius, double scaleFactor, boolean recomputeConnectivity)
          {
               gl = gldrawable.getGL();
               glu = gldrawable.getGLU();

               float[] defaultBondMat = {0.9f, 0.9f, 0.9f, 1.0f}; 

               gl.glTranslated(x0, y0, (z0 + zloc));

               gl.glRotated(theta, 0.0, 1.0, 0.0);
               gl.glRotated(phi, 0.0, 0.0, 1.0);
               gl.glRotated(psi, 1.0, 0.0, 0.0);

               // Draw atoms
               for (j = 0; j < natoms; j++)
               {
                    red = atomColors[(3*j)];
                    green = atomColors[(3*j) + 1];
                    blue = atomColors[(3*j) + 2];

                    material_alphaValue = 1.0f;

                    material = new float[4];

                    material[0] = red;
                    material[1] = green;
                    material[2] = blue;
                    material[3] = material_alphaValue; 

                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, material);

                    gl.glPushMatrix();

                    gl.glTranslated(coords[(3*j)], coords[(3*j) + 1], coords[(3*j) + 2]);

                    if (scaleRadius == 0)
                    {
                         if (scaleFactor*unscaledAtomRadius < bondRadius)
                         {
                              radius = bondRadius;
                         }
                         else
                         {
                              radius = scaleFactor*unscaledAtomRadius;
                         }

                         glut.glutSolidSphere(glu, radius, 100, 360);
                    }
                    else if (scaleRadius == 1)
                    {
                         if (scaleFactor*scaledAtomRadius[j] < bondRadius)
                         {
                              radius = bondRadius;   // don't draw atoms smaller than radius of a bond
                         }
                         else
                         {
                              radius = scaleFactor*scaledAtomRadius[j];
                         }

                         glut.glutSolidSphere(glu, radius, 100, 360);
                    }
                    else if (scaleRadius == 2)
                    {
                         glut.glutSolidSphere(glu, bondRadius, 100, 360);
                    }

                    gl.glPopMatrix();
               } 

               // Draw bonds
               if ( (bondsVisible == true) && (natoms > 1) )
               {
                    float[] bondmat = {0.9f, 0.9f, 0.9f, 1.0f};
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondmat);

                    // VFI atomic radii (from CrystalMaker 1.4.0)
                    double[ ] ar  =  { 0.46, 1.22, 1.55, 1.13, 0.91, 0.77, 0.71, 0.48, 0.42, 1.60, 1.89, 1.60, 1.43, 1.34, 1.30, 0.88, 0.79, 1.92, 2.36, 1.97, 1.64, 1.46, 1.34, 1.40, 1.31, 1.26, 1.25, 1.24, 1.28, 1.27, 1.39, 1.39, 1.48, 1.43, 0.94, 1.98, 2.48, 2.15, 1.81, 1.60, 1.45, 1.39, 1.36, 1.34, 1.34, 1.37, 1.44, 1.56, 1.66, 1.58, 1.61, 1.70, 1.15, 2.18, 2.68, 2.21, 1.87, 1.83, 1.82, 1.82, 2.05, 1.81, 2.02, 1.79, 1.77, 1.77, 1.76, 1.75, 1.75, 1.93, 1.74, 1.59, 1.46, 1.40, 1.37, 1.35, 1.35, 1.38, 1.44, 1.60, 1.71, 1.75, 1.82, 1.35, 1.27, 1.20, 1.00, 1.00, 2.03, 1.80, 1.62, 1.53, 1.50, 1.50, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };
                    double[ ] covrad  =  { 0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };         

                    if (recomputeConnectivity == true)
                    {
                         for (j = 1; j < natoms; j++)
                         {
                              for (k = 0; k < j; k++)
                              {
                                   if (zmatBondLength[j][k] > 0.0)
                                   {
                                        bondLength[j][k] = zmatBondLength[j][k];
                                   }
                                   else
                                   {
                                        bondLength[j][k] = 0.0;
                                   }
                              }
                         }

                         for (j = 1; j < natoms; j++)
                         {
                              for (k = 0; k < j; k++)
                              {
                                   if (bondLength[j][k] == 0.0)
                                   {
                                        dx = atomCoords[(3*j)] - atomCoords[(3*k)];
                                        dy = atomCoords[(3*j) + 1] - atomCoords[(3*k) + 1];
                                        dz = atomCoords[(3*j) + 2] - atomCoords[(3*k) + 2];
     
                                        r = Math.sqrt(dx*dx + dy*dy + dz*dz);

                                        ar1 = ar[(atomicNumber[j] - 1)];
                                        ar2 = ar[(atomicNumber[k] - 1)];
 
                                        if ( r <= 0.4*bondParam*(ar1 + ar2) )
                                        {
                                             bondLength[j][k] = r;
                                        }
                                   }
                              }
                         }

                         recomputeConnectivity = false;
                    }

                    // Draw cylindrical bonds
                    GLUquadric quadric = glu.gluNewQuadric();

                    double bd, r, r1, r2;
                    double angle, rot_x, rot_y, rot_z;                                        
                    double dx12, dy12, dz12;
                    double nx, ny, nz;
                    double n11, n22, n33;

                    gl.glTranslated(0.0, 0.0, 0.0);

                    for (j = 1; j < natoms; j++) 
                    {
                         for (k = 0; k < j; k++)
                         {   
                              if (bondLength[j][k] > 0.0)
                              { 
                                   bd = bondLength[j][k];

                                   gl.glPushMatrix();

                                   gl.glTranslated(coords[(3*k)], coords[(3*k) + 1], coords[(3*k) + 2]);
 
                                   dx12 = coords[(3*j)] - coords[(3*k)];
                                   dy12 = coords[(3*j) + 1] - coords[(3*k) + 1];
                                   dz12 = coords[(3*j) + 2] - coords[(3*k) + 2];
        
                                   r = Math.sqrt(dx12*dx12 + dy12*dy12);

                                   if (r > 0.0)
                                   {
                                        n11 = -dy12/r;
                                        n22 = dx12/r;
                                        n33 = 0.0;

                                        if (dz12 >= 0.0)
                                        {
                                             angle = (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                                        }
                                        else     
                                        {
                                             angle = 180.0 - (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                                        }
                                   }
                                   else
                                   {
                                        n11 = 0.0;
                                        n22 = 0.0;
                                        n33 = 1.0;

                                        if (dz12 >= 0.0)
                                        {
                                             angle = 0.0;
                                        }
                                        else     
                                        {
                                             angle = 180.0;
                                        }
                                   }

                                   if (bondsColored == false)
                                   {
                                        gl.glRotated(angle, n11, n22, n33);
 
                                        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, defaultBondMat); 
                                        glu.gluCylinder(quadric, bondRadius, bondRadius, bd, 20, 20); 
                                   }
                                   else
                                   {
                                        bondMat1[0] = atomColors[(3*k)];
                                        bondMat1[1] = atomColors[(3*k) + 1];
                                        bondMat1[2] = atomColors[(3*k) + 2];
                                        bondMat1[3] = material_alphaValue;

                                        bondMat2[0] = atomColors[(3*j)];
                                        bondMat2[1] = atomColors[(3*j) + 1];
                                        bondMat2[2] = atomColors[(3*j) + 2];
                                        bondMat2[3] = material_alphaValue;   

                                        gl.glRotated(angle, n11, n22, n33);

                                        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat1); 
                                        glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);

                                        gl.glRotated(-angle, n11, n22, n33);
                                        gl.glTranslated(dx12/2, dy12/2, dz12/2);
                                        gl.glRotated(angle, n11, n22, n33);
  
                                        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat2); 
                                        glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);
                                   }

                                   gl.glPopMatrix();
                              }    
                         }               
                    }             
               }
          }

          public void mousePressed(MouseEvent e)
          {
               //
          }

          public void mouseReleased(MouseEvent e)
          {
               //
          }

          public void mouseDragged(MouseEvent e)
          {
               draggedMouse = true;


mouseWasDragged = true;


               cntrlKeyDown = getCntrlKeyStatus();

               if (cntrlKeyDown)
               {
                   addBond = true;
               }
               else
               {
                   addBond = false;
               }

               axisSelectionDimen = 0.02;

               x2 = e.getX();
               y2 = e.getY();

               selectedNames = getSelectedNames();
               selectedFragmentAtoms = getSelectedFragmentAtoms();

               if ( (selectedNames.size() > 0) && (selectedFragmentAtoms.size() == 0) )
               {
                    x_rot = false;
               }
               else
               {
                    x_rot = true;
               }

               if ( (bondAngleSelected) || (dihedralAngleSelected) || (axialRotationSelected) || (bvDihedralSelected) )
               {
                    x_rot = true;
               }

               if ((altKeyDown == false) && (cntrlKeyDown == false))
               {
//                    if ( ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) && (x_rot == true) )
if ( ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) && ((e.getModifiers() & InputEvent.BUTTON3_MASK) != InputEvent.BUTTON3_MASK) )
                    {

                         int ndex, nrotatableAtoms, nrotatableAtoms1, nrotatableAtoms2, textFlag, fragbvNdx, targtNdx, bvndex;
                         double rotfrag_x, rotfrag_y, rotfrag_z;
                         double X, Y , Z, rotX, rotY, rotZ;
                         double bvX, bvY, bvZ, rotbvX, rotbvY, rotbvZ, Dbv;
                         double nnx, nny, nnz;
                         double bondHalfAngle;
                         double[][] bondRotMatrix = new double[ndimensions][ndimensions];

                         mouseButtonPressed = 1;
 
                         xyzKeys = getXYZKeyStatus();
                         keyNdx = xyzKeys.indexOf(1);

                         fragmentSelected = getFragmentSelectedStatus();

                         fragmentOrigin = getFragmentOrigin();

                         nrotatableAtoms = nrotatableAtoms1 = nrotatableAtoms2 = 0;
                         nnx = nny = nnz = 0.0;
                         bondHalfAngle = 0.0;
                         textFlag = -1;

                         delAlpha = delBeta = delOmega = 1.0;
                                              
                         if (axialRotationSelected)
                         {
                              axialFragmentAtoms = getAxialFragmentAtoms();

                              rotationOriginIndex = getRotationOriginIndex();

                              axialRotationAxis = getAxialRotationAxis();

                              nnx = axialRotationAxis[0];  nny = axialRotationAxis[1];  nnz = axialRotationAxis[2];
                         }
                         else if (bondAngleSelected)
                         {
                              rotationOriginIndex = getRotationOriginIndex();

                              bondAngleRotationAxis = getBondAngleRotationAxis();

                              bondAngleFragments = getBondAngleFragments();

                              nrotatableAtoms1 = bondAngleFragments1.size();
                              nrotatableAtoms2 = bondAngleFragments2.size();

                              nnx = bondAngleRotationAxis[0];  nny = bondAngleRotationAxis[1];  nnz = bondAngleRotationAxis[2];

                              fragmentBV = getFragmentBV();

                              textFlag = 3;
                         }
                         else if (dihedralAngleSelected)
                         {
                              rotationOriginIndex = getRotationOriginIndex();

                              dihedralAngleRotationAxis = getDihedralAngleRotationAxis();

                              dihedralAngleFragments = getDihedralAngleFragments();

                              nrotatableAtoms = dihedralAngleFragments.size();

                              nnx = dihedralAngleRotationAxis[0];  nny = dihedralAngleRotationAxis[1];  nnz = dihedralAngleRotationAxis[2];

                              delDihedralAngle = (Math.PI/180)*delEta;

                              fragmentBV = getFragmentBV();

                              textFlag = 4;
                         }

//                         delAlpha = delBeta = delOmega = 1.0;


//                         if (keyNdx == 2)             // z key pressed
                         if (keyNdx == 0)             // x key pressed
                         {
                              if ( (x2 - x1) > 0)
                              {
//                                   psi -= delPsi;
          //                         psi -= delPsi + rotAccel*Math.max(0, natoms - natomCutoff);

//                                   muz -= delMu + rotAccel*Math.max(0, natoms - natomCutoff);

                              }
                              else if ( (x2 - x1) < 0)
                              {
//                                   psi += delPsi;
          //                         psi += delPsi + rotAccel*Math.max(0, natoms - natomCutoff);

//                                   muz += delMu + rotAccel*Math.max(0, natoms - natomCutoff);

                              }

                              x1 = x2;
                         } 
                         else if (keyNdx == 1)        // y key pressed
                         {
                              if ( (x2 - x1) > 0) 
                              {
                                   if ( (!bondAngleSelected) && (!dihedralAngleSelected) && (!axialRotationSelected) )
                                   {
                                        if (fragmentSelected)
                                        {
                                             delAlpha = 1.0;
//                                             alpha += delAlpha;
                                             alpha += delAlpha + rotAccel*Math.max(0, natoms - natomCutoff);
                                        }
                                        else
                                        {
//                                             theta += delTheta;
          //                                   theta += delTheta + rotAccel*Math.max(0, natoms - natomCutoff);

//                                   muy += delMu + rotAccel*Math.max(0, natoms - natomCutoff);


                                        }
                                   }

                                   x1 = x2;
                              }
                              else if ( (x2 - x1) < 0)
                              {
                                   if ( (!bondAngleSelected) && (!dihedralAngleSelected) && (!axialRotationSelected) )
                                   {
                                        if (fragmentSelected)
                                        {
                                             delAlpha = -1.0;
//                                             alpha += delAlpha;
                                             alpha += delAlpha + rotAccel*Math.max(0, natoms - natomCutoff);
                                        }
                                        else
                                        {
//                                             theta -= delTheta;
          //                                   theta -= delTheta + rotAccel*Math.max(0, natoms - natomCutoff);

//                                   muy -= delMu + rotAccel*Math.max(0, natoms - natomCutoff);


                                        }
                                   }
                              }

                              x1 = x2;
//                              x1 = x2;  y1 = y2;
                         }
//                         else if (keyNdx == 0)        // x key pressed
                         else if (keyNdx == 2)        // z key pressed
                         {
                              if ( (x2 - x1) > 0) 
                              {
                                   if (fragmentSelected)
                                   {
                                        delBeta = 1.0;
//                                        beta += delBeta;
                                        beta += delBeta + rotAccel*Math.max(0, natoms - natomCutoff);
                                   }
                                   else
                                   {
//                                        phi += delPhi;
          //                              phi += delPhi + rotAccel*Math.max(0, natoms - natomCutoff);

//                                        mux += delMu + rotAccel*Math.max(0, natoms - natomCutoff);
                                   }
                              }
                              else if ( (x2 - x1) < 0)
                              {
                                   if (fragmentSelected)
                                   {
                                        delBeta = -1.0;
//                                        beta -= delBeta;
                                        beta -= delBeta + rotAccel*Math.max(0, natoms - natomCutoff);
                                   }
                                   else
                                   {
//                                        phi -= delPhi;
          //                              phi -= delPhi + rotAccel*Math.max(0, natoms - natomCutoff);

//                                        mux -= (delMu + rotAccel*Math.max(0, natoms - natomCutoff));
                                   }
                              }

                              x1 = x2;
                         }
                         else
                         {
                              if ( (x2 - x1) > 0) 
                              {
                                   if ( (!bondAngleSelected) && (!dihedralAngleSelected) && (!axialRotationSelected) )
                                   {
                                        if (fragmentSelected)
                                        {
                                             if (editMode)
                                             {
                                                  alpha += delAlpha;

                                                  for (j = 0; j < selectedFragmentAtoms.size(); j++)
                                                  {
                                                       indx = selectedFragmentAtoms.get(j);
  
                                                       rotX = atomCoordinates[indx][1] - fragmentOrigin.get(0); 
                                                       rotY = atomCoordinates[indx][2] - fragmentOrigin.get(1);                  
                                                       rotZ = atomCoordinates[indx][3] - fragmentOrigin.get(2);

                                                       rotfrag_x = rotX*Math.cos((Math.PI/180)*delAlpha) + rotZ*Math.sin((Math.PI/180)*delAlpha);
                                                       rotfrag_y = rotY;
                                                       rotfrag_z = (-rotX)*Math.sin((Math.PI/180)*delAlpha) + rotZ*Math.cos((Math.PI/180)*delAlpha);

                                                       for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                       {
                                                            bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );
  
                                                            if (bvndex == indx)
                                                            {
                                                                 bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                                 rotbvX = bvX*Math.cos((Math.PI/180)*delAlpha) + bvZ*Math.sin((Math.PI/180)*delAlpha);
                                                                 rotbvY = bvY;
                                                                 rotbvZ = (-bvX)*Math.sin((Math.PI/180)*delAlpha) + bvZ*Math.cos((Math.PI/180)*delAlpha);

                                                                 bondVectors[ndex][1] = rotbvX; 
                                                                 bondVectors[ndex][2] = rotbvY;
                                                                 bondVectors[ndex][3] = rotbvZ;
                                                           }
                                                       }

                                                       atomCoordinates[indx][1] = rotfrag_x + fragmentOrigin.get(0);
                                                       atomCoordinates[indx][2] = rotfrag_y + fragmentOrigin.get(1);
                                                       atomCoordinates[indx][3] = rotfrag_z + fragmentOrigin.get(2);
                                                  }

                                                  glcanvas.repaint();
                                             }
                                        }
                                        else
                                        {
                                             psi0 = psi;

                                             psi -= delPsi + rotAccel*Math.max(0, natoms - natomCutoff);

                                             deltaAngle = psi - psi0;
                                        }
                                   }

                                   else if (axialRotationSelected)                // NEED TO INCLUDE THIS HERE!!!!!!
                                   {
                                        if (editMode)
                                        {
                                             delAxialAngle = (Math.PI/180)*deltaDihedral;

                                             for (ndx = 0; ndx < axialFragmentAtoms.size(); ndx++)
                                             {
                                                  indx = axialFragmentAtoms.get(ndx);

                                                   X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                   Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                   Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                   // Compute rotation matrix
                                                   bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(delAxialAngle) * (1 - nnx * nnx) );
                                                   bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) + (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) - (nny * Math.sin(delAxialAngle));

                                                   bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) - (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(delAxialAngle) * (1 - nny * nny) );
                                                   bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) + (nnx * Math.sin(delAxialAngle));
  
                                                   bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) + (nny * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) - (nnx * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(delAxialAngle) * (1 - nnz * nnz) );

                                                   rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                   rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                   rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                   atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                   atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                   atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];


                                                   for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                   {
                                                        bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                                                        if (bvndex == indx)
                                                        {
                                                             bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                             bondVectors[ndex][1] = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                             bondVectors[ndex][2] = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                             bondVectors[ndex][3] = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                        }
                                                   }
                                              }
                                         }
                                   }

                              }

 
                              else if ( (x2 - x1) < 0)
                              {
                                   if ( (!bondAngleSelected) && (!dihedralAngleSelected) && (!axialRotationSelected) )
                                   {
                                        if (fragmentSelected)
                                        {
                                             if (editMode)
                                             {
//                                                  alpha -= delAlpha;
                                                  alpha -= delAlpha + rotAccel*Math.max(0, natoms - natomCutoff);

                                                  for (j = 0; j < selectedFragmentAtoms.size(); j++)
                                                  {
                                                       indx = selectedFragmentAtoms.get(j);

                                                       rotX = atomCoordinates[indx][1] - fragmentOrigin.get(0); 
                                                       rotY = atomCoordinates[indx][2] - fragmentOrigin.get(1);                  
                                                       rotZ = atomCoordinates[indx][3] - fragmentOrigin.get(2);
 
                                                       rotfrag_x = rotX*Math.cos((Math.PI/180)*delAlpha) - rotZ*Math.sin((Math.PI/180)*delAlpha);
                                                       rotfrag_y = rotY;
                                                       rotfrag_z = rotX*Math.sin((Math.PI/180)*delAlpha) + rotZ*Math.cos((Math.PI/180)*delAlpha);

                                                       for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                       {
                                                            bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );
  
                                                            if (bvndex == indx)
                                                            {
                                                                 bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                                 rotbvX = bvX*Math.cos((Math.PI/180)*delAlpha) - bvZ*Math.sin((Math.PI/180)*delAlpha);
                                                                 rotbvY = bvY;
                                                                 rotbvZ = (bvX)*Math.sin((Math.PI/180)*delAlpha) + bvZ*Math.cos((Math.PI/180)*delAlpha);

                                                                 bondVectors[ndex][1] = rotbvX; 
                                                                 bondVectors[ndex][2] = rotbvY;
                                                                 bondVectors[ndex][3] = rotbvZ;
                                                           }
                                                       }

                                                       atomCoordinates[indx][1] = rotfrag_x + fragmentOrigin.get(0);
                                                       atomCoordinates[indx][2] = rotfrag_y + fragmentOrigin.get(1);
                                                       atomCoordinates[indx][3] = rotfrag_z + fragmentOrigin.get(2);
                                                  }

                                                  glcanvas.repaint();
                                             }
                                        }
                                        else
                                        {
                                             psi0 = psi;

                                             psi += delPsi + rotAccel*Math.max(0, natoms - natomCutoff);

                                             deltaAngle = psi - psi0;
                                        }
                                   }
                                   else if (axialRotationSelected)                // NEED TO INCLUDE THIS HERE!!!!!!
                                   {
                                        if (editMode)
                                        {
                                             delAxialAngle = -(Math.PI/180)*deltaDihedral;

                                             for (ndx = 0; ndx < axialFragmentAtoms.size(); ndx++)
                                             {
                                                  indx = axialFragmentAtoms.get(ndx);

                                                   X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                   Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                   Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                   // Compute rotation matrix
                                                   bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(delAxialAngle) * (1 - nnx * nnx) );
                                                   bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) + (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) - (nny * Math.sin(delAxialAngle));

                                                   bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) - (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(delAxialAngle) * (1 - nny * nny) );
                                                   bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) + (nnx * Math.sin(delAxialAngle));
  
                                                   bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) + (nny * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) - (nnx * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(delAxialAngle) * (1 - nnz * nnz) );

                                                   rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                   rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                   rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                   atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                   atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                   atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];


                                                   for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                   {
                                                        bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                                                        if (bvndex == indx)
                                                        {
                                                             bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                             bondVectors[ndex][1] = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                             bondVectors[ndex][2] = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                             bondVectors[ndex][3] = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                        }
                                                   }
                                              }
                                         }
                                   }
                              }


//                              if ( (y2 - y1) > 0)  
                              else if ( (y2 - y1) > 0)  
                              {
                                   if ( (!bondAngleSelected) && (!dihedralAngleSelected) && (!axialRotationSelected) )
                                   {
                                        if (fragmentSelected)
                                        {
                                             if (editMode)
                                             {
//                                                  beta += delBeta;
                                                  beta += delBeta + rotAccel*Math.max(0, natoms - natomCutoff);  

                                                  for (j = 0; j < selectedFragmentAtoms.size(); j++)
                                                  {
                                                       indx = selectedFragmentAtoms.get(j);
 
                                                       rotX = atomCoordinates[indx][1] - fragmentOrigin.get(0); 
                                                       rotY = atomCoordinates[indx][2] - fragmentOrigin.get(1);                  
                                                       rotZ = atomCoordinates[indx][3] - fragmentOrigin.get(2);
  
                                                       rotfrag_x = rotX*Math.cos((Math.PI/180)*delBeta) + rotY*Math.sin((Math.PI/180)*delBeta);
                                                       rotfrag_y = (-rotX)*Math.sin((Math.PI/180)*delBeta) + rotY*Math.cos((Math.PI/180)*delBeta);
                                                       rotfrag_z = rotZ;
 
                                                       for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                       {
                                                            bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );
  
                                                            if (bvndex == indx)
                                                            {
                                                                 bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                                 rotbvX = bvX*Math.cos((Math.PI/180)*delBeta) + bvY*Math.sin((Math.PI/180)*delBeta);
                                                                 rotbvY = (-bvX)*Math.sin((Math.PI/180)*delBeta) + bvY*Math.cos((Math.PI/180)*delBeta);
                                                                 rotbvZ = bvZ;

                                                                 bondVectors[ndex][1] = rotbvX; 
                                                                 bondVectors[ndex][2] = rotbvY;
                                                                 bondVectors[ndex][3] = rotbvZ;
                                                           }
                                                       }

                                                       atomCoordinates[indx][1] = rotfrag_x + fragmentOrigin.get(0);
                                                       atomCoordinates[indx][2] = rotfrag_y + fragmentOrigin.get(1);
                                                       atomCoordinates[indx][3] = rotfrag_z + fragmentOrigin.get(2);
                                                  }

                                                  glcanvas.repaint();
                                             }
                                        }
                                        else
                                        {
//                                             phi -= delPhi;
          //                                   phi -= delPhi + rotAccel*Math.max(0, natoms - natomCutoff);
                                        }

                                        x1 = x2; y1 = y2;
                                   }
/*

//                                   else if (axialRotationSelected)
                                   else if ( (axialRotationSelected) && ((x2 - x1) == 0) )
                                   {
                                        if (editMode)
                                        {
                                             delAxialAngle = (Math.PI/180)*deltaDihedral;

                                             for (ndx = 0; ndx < axialFragmentAtoms.size(); ndx++)
                                             {
                                                  indx = axialFragmentAtoms.get(ndx);

                                                   X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                   Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                   Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                   // Compute rotation matrix
                                                   bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(delAxialAngle) * (1 - nnx * nnx) );
                                                   bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) + (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) - (nny * Math.sin(delAxialAngle));

                                                   bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) - (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(delAxialAngle) * (1 - nny * nny) );
                                                   bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) + (nnx * Math.sin(delAxialAngle));
  
                                                   bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) + (nny * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) - (nnx * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(delAxialAngle) * (1 - nnz * nnz) );

                                                   rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                   rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                   rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                   atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                   atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                   atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];


                                                   for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                   {
                                                        bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                                                        if (bvndex == indx)
                                                        {
                                                             bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                             bondVectors[ndex][1] = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                             bondVectors[ndex][2] = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                             bondVectors[ndex][3] = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                        }
                                                   }
                                              }
                                         }
                                   }
*/
                                   else if (bondAngleSelected)
                                   {   
                                        if (editMode)
                                        { 
                                             bondHalfAngle = (Math.PI/180)*(delEpsilon);

                                             // Compute rotation matrix for counterclockwise rotation
                                             bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(bondHalfAngle) * (1 - nnx * nnx) );
                                             bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(bondHalfAngle)) ) + (nnz * Math.sin(bondHalfAngle));
                                             bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(bondHalfAngle)) ) - (nny * Math.sin(bondHalfAngle));

                                             bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(bondHalfAngle)) ) - (nnz * Math.sin(bondHalfAngle));
                                             bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(bondHalfAngle) * (1 - nny * nny) );
                                             bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(bondHalfAngle)) ) + (nnx * Math.sin(bondHalfAngle));
  
                                             bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(bondHalfAngle)) ) + (nny * Math.sin(bondHalfAngle));
                                             bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(bondHalfAngle)) ) - (nnx * Math.sin(bondHalfAngle));
                                             bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(bondHalfAngle) * (1 - nnz * nnz) );

                                             for (ndx = 0; ndx < nrotatableAtoms1; ndx++)
                                             {
                                                  indx = bondAngleFragments1.get(ndx);

                                                  X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                  Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                  Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                  rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                  rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                  rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                  atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                  atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                  atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                                                  for (fragbvNdx = 0; fragbvNdx < fragmentBV.size(); fragbvNdx++)
                                                  {
                                                       targtNdx = ( -((int)(bondVectors[fragbvNdx][0]/100) + 1) );

                                                       if (targtNdx == indx)
                                                       {
                                                            bvX = bondVectors[fragbvNdx][1];
                                                            bvY = bondVectors[fragbvNdx][2];
                                                            bvZ = bondVectors[fragbvNdx][3];

                                                            rotbvX = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                            rotbvY = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                            rotbvZ = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                            
                                                            bondVectors[fragbvNdx][1] = rotbvX;
                                                            bondVectors[fragbvNdx][2] = rotbvY;
                                                            bondVectors[fragbvNdx][3] = rotbvZ;
                                                       }
                                                  }
                                             }

                                             // Compute rotation matrix for clockwise rotation
                                             bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(-bondHalfAngle) * (1 - nnx * nnx) );
                                             bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(-bondHalfAngle)) ) + (nnz * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(-bondHalfAngle)) ) - (nny * Math.sin(-bondHalfAngle));

                                             bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(-bondHalfAngle)) ) - (nnz * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(-bondHalfAngle) * (1 - nny * nny) );
                                             bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(-bondHalfAngle)) ) + (nnx * Math.sin(-bondHalfAngle));
  
                                             bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(-bondHalfAngle)) ) + (nny * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(-bondHalfAngle)) ) - (nnx * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(-bondHalfAngle) * (1 - nnz * nnz) );

                                             for (ndx = 0; ndx < nrotatableAtoms2; ndx++)
                                             {
                                                  indx = bondAngleFragments2.get(ndx);

                                                  X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                  Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                  Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                  rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                  rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                  rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                  atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                  atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                  atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                                                  for (fragbvNdx = 0; fragbvNdx < fragmentBV.size(); fragbvNdx++)
                                                  {
                                                       targtNdx = ( -((int)(bondVectors[fragbvNdx][0]/100) + 1) );

                                                       if (targtNdx == indx)
                                                       {
                                                            bvX = bondVectors[fragbvNdx][1];
                                                            bvY = bondVectors[fragbvNdx][2];
                                                            bvZ = bondVectors[fragbvNdx][3];

                                                            rotbvX = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                            rotbvY = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                            rotbvZ = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                            
                                                            bondVectors[fragbvNdx][1] = rotbvX;
                                                            bondVectors[fragbvNdx][2] = rotbvY;
                                                            bondVectors[fragbvNdx][3] = rotbvZ;
                                                       }
                                                  }
                                             }

                                             writeSelectedAttributes(selectedComponents, textFlag);
                                        }
                                   }
                                   else if (dihedralAngleSelected)
                                   { 
                                        if (editMode)
                                        {
                                             if (bvDihedralSelected)
                                             {
                                                  delDihedralAngle = (Math.PI/180)*deltaDihedral;
                                             }

                                             // Compute rotation matrix
                                             bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(delDihedralAngle) * (1 - nnx * nnx) );
                                             bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(delDihedralAngle)) ) + (nnz * Math.sin(delDihedralAngle));
                                             bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(delDihedralAngle)) ) - (nny * Math.sin(delDihedralAngle));

                                             bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(delDihedralAngle)) ) - (nnz * Math.sin(delDihedralAngle));
                                             bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(delDihedralAngle) * (1 - nny * nny) );
                                             bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(delDihedralAngle)) ) + (nnx * Math.sin(delDihedralAngle));
  
                                             bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(delDihedralAngle)) ) + (nny * Math.sin(delDihedralAngle));
                                             bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(delDihedralAngle)) ) - (nnx * Math.sin(delDihedralAngle));
                                             bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(delDihedralAngle) * (1 - nnz * nnz) );

                                             for (ndx = 0; ndx < nrotatableAtoms; ndx++)
                                             {
                                                  indx = dihedralAngleFragments.get(ndx);

                                                  X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                  Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                  Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                  rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                  rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                  rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                  atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                  atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                  atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                                                  for (fragbvNdx = 0; fragbvNdx < fragmentBV.size(); fragbvNdx++)
                                                  {
                                                       targtNdx = ( -((int)(bondVectors[fragbvNdx][0]/100) + 1) );

                                                       if (targtNdx == indx)
                                                       {
                                                            bvX = bondVectors[fragbvNdx][1];
                                                            bvY = bondVectors[fragbvNdx][2];
                                                            bvZ = bondVectors[fragbvNdx][3];

                                                            rotbvX = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                            rotbvY = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                            rotbvZ = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                            
                                                            bondVectors[fragbvNdx][1] = rotbvX;
                                                            bondVectors[fragbvNdx][2] = rotbvY;
                                                            bondVectors[fragbvNdx][3] = rotbvZ;
                                                       }
                                                  }

                                             }

                                             writeSelectedAttributes(selectedComponents, textFlag);
                                        }
                                   }

                                   x1 = x2; y1 = y2;
                              }

//                             if ( (y2 - y1) < 0 )
                              else if ( (y2 - y1) < 0 )
                              {
                                   if ( (!bondAngleSelected) && (!dihedralAngleSelected) && (!axialRotationSelected) )
                                   {
                                        if (fragmentSelected)
                                        {
                                             if (editMode)
                                             {
//                                                  beta -= delBeta;
                                                  beta -= delBeta + rotAccel*Math.max(0, natoms - natomCutoff);
 
                                                  for (j = 0; j < selectedFragmentAtoms.size(); j++)
                                                  {
                                                       indx = selectedFragmentAtoms.get(j);

                                                       rotX = atomCoordinates[indx][1] - fragmentOrigin.get(0); 
                                                       rotY = atomCoordinates[indx][2] - fragmentOrigin.get(1);                  
                                                       rotZ = atomCoordinates[indx][3] - fragmentOrigin.get(2);

                                                       rotfrag_x = rotX*Math.cos((Math.PI/180)*delBeta) - rotY*Math.sin((Math.PI/180)*delBeta);
                                                       rotfrag_y = rotX*Math.sin((Math.PI/180)*delBeta) + rotY*Math.cos((Math.PI/180)*delBeta);
                                                       rotfrag_z = rotZ;

                                                       for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                       {
                                                            bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );
  
                                                            if (bvndex == indx)
                                                            {
                                                                 bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                                 rotbvX = bvX*Math.cos((Math.PI/180)*delBeta) - bvY*Math.sin((Math.PI/180)*delBeta);
                                                                 rotbvY = bvX*Math.sin((Math.PI/180)*delBeta) + bvY*Math.cos((Math.PI/180)*delBeta);
                                                                 rotbvZ = bvZ;

                                                                 bondVectors[ndex][1] = rotbvX; 
                                                                 bondVectors[ndex][2] = rotbvY;
                                                                 bondVectors[ndex][3] = rotbvZ;
                                                           }
                                                       }

                                                       atomCoordinates[indx][1] = rotfrag_x + fragmentOrigin.get(0);
                                                       atomCoordinates[indx][2] = rotfrag_y + fragmentOrigin.get(1);
                                                       atomCoordinates[indx][3] = rotfrag_z + fragmentOrigin.get(2);
                                                    }

                                                  glcanvas.repaint();
                                             }
                                        }
                                        else
                                        {
//                                             phi += delPhi;
          //                                   phi += delPhi + rotAccel*Math.max(0, natoms - natomCutoff);
                                        }
                              
                                        x1 = x2;  y1 = y2;
                                   }                                    
/*
                                   else if ( (axialRotationSelected) && ((x2 - x1) == 0) )
                                   {
                                        if (editMode)
                                        {
                                             delAxialAngle = -(Math.PI/180)*deltaDihedral;

                                             for (ndx = 0; ndx < axialFragmentAtoms.size(); ndx++)
                                             {
                                                  indx = axialFragmentAtoms.get(ndx);

                                                   X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                   Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                   Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                   // Compute rotation matrix
                                                   bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(delAxialAngle) * (1 - nnx * nnx) );
                                                   bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) + (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) - (nny * Math.sin(delAxialAngle));

                                                   bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(delAxialAngle)) ) - (nnz * Math.sin(delAxialAngle));
                                                   bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(delAxialAngle) * (1 - nny * nny) );
                                                   bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) + (nnx * Math.sin(delAxialAngle));
  
                                                   bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(delAxialAngle)) ) + (nny * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(delAxialAngle)) ) - (nnx * Math.sin(delAxialAngle));
                                                   bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(delAxialAngle) * (1 - nnz * nnz) );

                                                   rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                   rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                   rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                   atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                   atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                   atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];


                                                   for (ndex = 0; ndex < bondVectors.length; ndex++)
                                                   {
                                                        bvndex = ( -((int)(bondVectors[ndex][0]/100) + 1) );

                                                        if (bvndex == indx)
                                                        {
                                                             bvX = bondVectors[ndex][1];  bvY = bondVectors[ndex][2];  bvZ = bondVectors[ndex][3];  

                                                             bondVectors[ndex][1] = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                             bondVectors[ndex][2] = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                             bondVectors[ndex][3] = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                        }
                                                   }
                                              }
                                         }
                                   }
*/
                                   else if (bondAngleSelected)
                                   {   
                                        if (editMode)
                                        { 
                                             bondHalfAngle = -(Math.PI/180)*(delEpsilon);

                                             // Compute rotation matrix for counterclockwise rotation
                                             bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(bondHalfAngle) * (1 - nnx * nnx) );
                                             bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(bondHalfAngle)) ) + (nnz * Math.sin(bondHalfAngle));
                                             bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(bondHalfAngle)) ) - (nny * Math.sin(bondHalfAngle));

                                             bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(bondHalfAngle)) ) - (nnz * Math.sin(bondHalfAngle));
                                             bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(bondHalfAngle) * (1 - nny * nny) );
                                             bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(bondHalfAngle)) ) + (nnx * Math.sin(bondHalfAngle));
  
                                             bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(bondHalfAngle)) ) + (nny * Math.sin(bondHalfAngle));
                                             bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(bondHalfAngle)) ) - (nnx * Math.sin(bondHalfAngle));
                                             bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(bondHalfAngle) * (1 - nnz * nnz) );

                                             for (ndx = 0; ndx < nrotatableAtoms1; ndx++)
                                             {
                                                  indx = bondAngleFragments1.get(ndx);

                                                  X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                  Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                  Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                  rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                  rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                  rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                  atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                  atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                  atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                                                  for (fragbvNdx = 0; fragbvNdx < fragmentBV.size(); fragbvNdx++)
                                                  {
                                                       targtNdx = ( -((int)(bondVectors[fragbvNdx][0]/100) + 1) );

                                                       if (targtNdx == indx)
                                                       {
                                                            bvX = bondVectors[fragbvNdx][1];
                                                            bvY = bondVectors[fragbvNdx][2];
                                                            bvZ = bondVectors[fragbvNdx][3];

                                                            rotbvX = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                            rotbvY = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                            rotbvZ = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;
                                                            
                                                            bondVectors[fragbvNdx][1] = rotbvX;
                                                            bondVectors[fragbvNdx][2] = rotbvY;
                                                            bondVectors[fragbvNdx][3] = rotbvZ;
                                                       }
                                                  }
                                             }

                                             // Compute rotation matrix for clockwise rotation
                                             bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(-bondHalfAngle) * (1 - nnx * nnx) );
                                             bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(-bondHalfAngle)) ) + (nnz * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(-bondHalfAngle)) ) - (nny * Math.sin(-bondHalfAngle));

                                             bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(-bondHalfAngle)) ) - (nnz * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(-bondHalfAngle) * (1 - nny * nny) );
                                             bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(-bondHalfAngle)) ) + (nnx * Math.sin(-bondHalfAngle));
  
                                             bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(-bondHalfAngle)) ) + (nny * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(-bondHalfAngle)) ) - (nnx * Math.sin(-bondHalfAngle));
                                             bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(-bondHalfAngle) * (1 - nnz * nnz) );

                                             for (ndx = 0; ndx < nrotatableAtoms2; ndx++)
                                             {
                                                  indx = bondAngleFragments2.get(ndx);

                                                  X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                  Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                  Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                  rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                  rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                  rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                  atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                  atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                  atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                                                  for (fragbvNdx = 0; fragbvNdx < fragmentBV.size(); fragbvNdx++)
                                                  {
                                                       targtNdx = ( -((int)(bondVectors[fragbvNdx][0]/100) + 1) );

                                                       if (targtNdx == indx)
                                                       {
                                                            bvX = bondVectors[fragbvNdx][1];
                                                            bvY = bondVectors[fragbvNdx][2];
                                                            bvZ = bondVectors[fragbvNdx][3];

                                                            rotbvX = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                            rotbvY = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                            rotbvZ = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;

                                                            bondVectors[fragbvNdx][1] = rotbvX;
                                                            bondVectors[fragbvNdx][2] = rotbvY;
                                                            bondVectors[fragbvNdx][3] = rotbvZ;
                                                       }
                                                  }
                                             }

                                             writeSelectedAttributes(selectedComponents, textFlag);
                                        }

                                        x1 = x2;  y1 = y2;
                                   }
                                   else if (dihedralAngleSelected)
                                   { 
                                        if (editMode)
                                        {
                                             if (bvDihedralSelected)
                                             {
                                                  delDihedralAngle = (Math.PI/180)*deltaDihedral;
                                             }

                                             // Compute rotation matrix
                                             bondRotMatrix[0][0] = (nnx * nnx) + ( Math.cos(-delDihedralAngle) * (1 - nnx * nnx) );
                                             bondRotMatrix[0][1] = ( (nnx * nny) * (1 - Math.cos(-delDihedralAngle)) ) + (nnz * Math.sin(-delDihedralAngle));
                                             bondRotMatrix[0][2] = ( (nnz * nnx) * (1 - Math.cos(-delDihedralAngle)) ) - (nny * Math.sin(-delDihedralAngle));

                                             bondRotMatrix[1][0] = ( (nnx * nny) * (1 - Math.cos(-delDihedralAngle)) ) - (nnz * Math.sin(-delDihedralAngle));
                                             bondRotMatrix[1][1] = (nny * nny) + ( Math.cos(-delDihedralAngle) * (1 - nny * nny) );
                                             bondRotMatrix[1][2] = ( (nny * nnz) * (1 - Math.cos(-delDihedralAngle)) ) + (nnx * Math.sin(-delDihedralAngle));
  
                                             bondRotMatrix[2][0] = ( (nnz * nnx) * (1 - Math.cos(-delDihedralAngle)) ) + (nny * Math.sin(-delDihedralAngle));
                                             bondRotMatrix[2][1] = ( (nny * nnz) * (1 - Math.cos(-delDihedralAngle)) ) - (nnx * Math.sin(-delDihedralAngle));
                                             bondRotMatrix[2][2] = (nnz * nnz) + ( Math.cos(-delDihedralAngle) * (1 - nnz * nnz) );

                                             for (ndx = 0; ndx < nrotatableAtoms; ndx++)
                                             {
                                                  indx = dihedralAngleFragments.get(ndx);

                                                  X = atomCoordinates[indx][1] - atomCoordinates[rotationOriginIndex][1];
                                                  Y = atomCoordinates[indx][2] - atomCoordinates[rotationOriginIndex][2];
                                                  Z = atomCoordinates[indx][3] - atomCoordinates[rotationOriginIndex][3];

                                                  rotX = bondRotMatrix[0][0]*X + bondRotMatrix[0][1]*Y + bondRotMatrix[0][2]*Z;
                                                  rotY = bondRotMatrix[1][0]*X + bondRotMatrix[1][1]*Y + bondRotMatrix[1][2]*Z;
                                                  rotZ = bondRotMatrix[2][0]*X + bondRotMatrix[2][1]*Y + bondRotMatrix[2][2]*Z;

                                                  atomCoordinates[indx][1] = rotX + atomCoordinates[rotationOriginIndex][1];
                                                  atomCoordinates[indx][2] = rotY + atomCoordinates[rotationOriginIndex][2];
                                                  atomCoordinates[indx][3] = rotZ + atomCoordinates[rotationOriginIndex][3];

                                                  for (fragbvNdx = 0; fragbvNdx < fragmentBV.size(); fragbvNdx++)
                                                  {
                                                       targtNdx = ( -((int)(bondVectors[fragbvNdx][0]/100) + 1) );

                                                       if (targtNdx == indx)
                                                       {
                                                            bvX = bondVectors[fragbvNdx][1];
                                                            bvY = bondVectors[fragbvNdx][2];
                                                            bvZ = bondVectors[fragbvNdx][3];

                                                            rotbvX = bondRotMatrix[0][0]*bvX + bondRotMatrix[0][1]*bvY + bondRotMatrix[0][2]*bvZ;
                                                            rotbvY = bondRotMatrix[1][0]*bvX + bondRotMatrix[1][1]*bvY + bondRotMatrix[1][2]*bvZ;
                                                            rotbvZ = bondRotMatrix[2][0]*bvX + bondRotMatrix[2][1]*bvY + bondRotMatrix[2][2]*bvZ;

                                                            bondVectors[fragbvNdx][1] = rotbvX;
                                                            bondVectors[fragbvNdx][2] = rotbvY;
                                                            bondVectors[fragbvNdx][3] = rotbvZ;
                                                       }
                                                  }
                                             }

                                             writeSelectedAttributes(selectedComponents, textFlag);
                                        }
                                   }
                              }

                              x1 = x2;  y1 = y2;
                         }
                    }
//                    else if ( ((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK) && (!fragmentSelected) )
                    else if ((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK)
                    {
                         mouseButtonPressed = 2;

                         if ( (x2 - x1) > 0) 
                         {
                            phi -= delPhi + rotAccel*Math.max(0, natoms - natomCutoff);   
//rotAxis = "z";


                         }
                         else if ( (x2 - x1) < 0) 
                         {
                              phi += delPhi + rotAccel*Math.max(0, natoms - natomCutoff);   

//rotAxis = "z";
                         }

                         x1 = x2;
                    }

//                    else if ( ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) && (!fragmentSelected) )
                    else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
                    {
                         mouseButtonPressed = 3;
                       
                         // Rotate about y-axis if mouse button 3 only is pressed
                         if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != InputEvent.BUTTON1_MASK)
                         //if (!tKeyDown)
                         {
                              if ( (x2 - x1) > 0 )
                              {
                                   theta += delTheta + rotAccel*Math.max(0, natoms - natomCutoff);
//rotAxis = "y";
                              }
                              else if ( (x2 - x1) < 0 )
                              {
                                   theta -= delTheta + rotAccel*Math.max(0, natoms - natomCutoff);
//rotAxis = "y";
                              }

                              x1 = x2;
                         }
                         else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
                         {
                              // Zoom structure if both mouse buttons 1 and 3 are pressed simultaneously
                              if (!editMode)
                              {
                                   if ( (y2 - y1) > 0 )
                                   {
//                                      zloc -= 0.10;
//                                      zloc -= 0.25;
//                                      zloc -= 0.60;

                                        if (natoms <= natomCutoff)
                                        {
                                             molScaleFactor += 0.02;
                                        } 
                                        else
                                        {
                                             molScaleFactor += 0.075 + msfAccel*Math.max(0, natoms - natomCutoff);;
                                        }
                                   }
                                   else if ( (y2 - y1) < 0 )
                                   {
//                                        zloc += 0.10;
//                                        zloc += 0.25;
//                                        zloc += 0.60;
  
                                        if (natoms <= natomCutoff)
                                        {
                                             molScaleFactor -= 0.02;
                                        } 
                                        else
                                        {
                                             molScaleFactor -= 0.075 + msfAccel*Math.max(0, natoms - natomCutoff);;
                                        }
                                   }
 
                                   y1 = y2;
                              }

                         else if ( (editMode) && (!bondAngleSelected) && (!dihedralAngleSelected) && (!axialRotationSelected) && (!bvDihedralSelected) )
                         {
                              selectedNames = getSelectedNames();
 
                              natomsSelected = selectedNames.size();

                              uniqueFragmentID = true;

                              if (natomsSelected > 0)
                              {               
                                   fragmentID = (int)atomCoordinates[selectedNames.get(0)][4];
                                   uniqueFragmentID = true;

                                   for (j = 1; j < natomsSelected; j++)
                                   {
                                        if (atomCoordinates[selectedNames.get(j)][4] != fragmentID)
                                        {
                                             uniqueFragmentID = false;  
                                        }
                                   }
                              }       

                              if (uniqueFragmentID)
                              {
                                   delta = 0.10;

                                   if ( (x2 - x1) > 0 )
                                   {
                                        for (j = 0; j < natomsSelected; j++)
                                        {
                                             atomCoordinates[selectedNames.get(j)][1] += delta*Math.cos((Math.PI/180)*theta);
                                             atomCoordinates[selectedNames.get(j)][3] += delta*Math.sin((Math.PI/180)*theta);
                                        }
                                   }
                                   else if ( (x2 - x1) < 0 )
                                   {
                                        for (j = 0; j < natomsSelected; j++)
                                        {
                                             atomCoordinates[selectedNames.get(j)][1] -= delta*Math.cos((Math.PI/180)*theta);
                                             atomCoordinates[selectedNames.get(j)][3] -= delta*Math.sin((Math.PI/180)*theta);
                                        }
                                   }

                                   x1 = x2;

                                   if ( (y2 - y1) > 0 )
                                   {
                                        for (j = 0; j < natomsSelected; j++)
                                        {
                                             atomCoordinates[selectedNames.get(j)][2] -= delta;
                                        }
                                   }
                                   else if ( (y2 - y1) < 0 )
                                   {
                                        for (j = 0; j < natomsSelected; j++)
                                        {
                                             atomCoordinates[selectedNames.get(j)][2] += delta;
                                        }
                                   }

                                   y1 = y2;
                              }

                              setFragmentOrigin(atomCoordinates, selectedNames);

                         }
}

                    }     

                    // Hide any popup mens that are visible
                    hidePopupMenus();

                    // Hide any submenus that are visible
                    hidePopupSubmenus();

                    glcanvas.repaint();
               }
          }

          public void mouseMoved(MouseEvent e)
          {
          }

          public void reshape(GLDrawable gldraw, int x, int y, int width, int height)
          {
          }

          public void displayChanged(GLDrawable gldraw, boolean modeChanged, boolean deviceChanged)
          {
          }
     }

     class stopTimer extends TimerTask
     {
          java.util.Timer timer = new java.util.Timer();

          Calendar now = Calendar.getInstance();

          public void run()
          {
               timer.cancel();
               timerRunning = false;
               msec2 = now.getTimeInMillis();
          }
     }

     class CanvasMouseListener extends MouseAdapter 
     {
          public void mousePressed(MouseEvent e)
          {
               mouseDownCoords[0] = e.getX();
               mouseDownCoords[1] = e.getY();

               selectedNames = getSelectedNames();

               if (mouseButtonClicked == 3)
               {

               }
               else if (mouseButtonClicked == 1)
               {
                    bondAngleSelected = getBondAngleSelectedStatus();

                    if (bondAngleSelected)
                    {
                         if (editMode)
                         {
                              bondAngleFragments = getBondAngleFragments();
                              bondAngleRotationAxis = getBondAngleRotationAxis();

                              rotationOriginIndex = getRotationOriginIndex();

                              bondAngleFragments1 = bondAngleFragments.get(0);
                              bondAngleFragments2 = bondAngleFragments.get(1);
                         }
                         else
                         {
                              bondAngleFragments2.clear();
                              bondAngleFragments.clear();  bondAngleFragments1.clear();
                         }
                    }               
               }

               glcanvas.repaint();
          }

          public void mouseReleased(MouseEvent e)
          {
               double delMouseX, delMouseY;

               mouseUpCoords[0] = e.getX();
               mouseUpCoords[1] = e.getY();

               mouseButtonPressed = 0;

               releasedMouse = true;

/*

if (mouseWasDragged)
{
     generateMol2File(moleculeName);
}

System.out.println("molecule name = " + moleculeName);

//mouseWasDragged = false;
System.out.println("currentMol2Filename = " + currentMol2Filename);


if (rotatedStructure)
{
     rotatedStructure = false;
     globalAxisRotation = false;
}

*/


               theta0 = getTheta();  phi0 = getPhi();  psi0 = getPsi();
               theta = getTheta();  phi = getPhi();  psi = getPsi();

               // Generate new mol2 file if molecule being created is rotated
               if (userDefnbvs.size() > 0)
               {
                    if ( (nhits == 0) && (draggedMouse) )
                    {
                         double prevTheta, prevPhi, prevPsi;
                         double deltheta, delphi, delpsi;

                         // Get current rotation angles for molecule

                         // Get rotation angles for molecule in last userDefFileSuffix 
                         prevTheta = usrdefTheta.get( (userDefFileSuffix - 1) );
                         prevPhi = usrdefPhi.get( (userDefFileSuffix - 1) );
                         prevPsi = usrdefPsi.get( (userDefFileSuffix - 1) );

                         // Determine if molecule has been rotation since addition of last atom or functional group
                         deltheta = theta - prevTheta;
                         delphi = phi - prevPhi;
                         delpsi = psi - prevPsi;

                         // If molecule has been rotated, generate new mol2 file for rotated molecule
                         if ( (deltheta != 0) || (delpsi != 0) || (delpsi != 0) )
                         {
                              generateMol2File(moleculeName);
                         }
                    }
               }

               selectedNames = getSelectedNames();

               glcanvas.repaint();

               if (draggedMouse)
               {
                    selectedNames = getSelectedNames();

                    if (selectedNames.size() > 0)
                    {
                         translateAtoms = true;

                         translatedAtomIndices.clear();

                         for (ndx = 0; ndx < selectedNames.size(); ndx++)
                         {
                             translatedAtomIndices.add(selectedNames.get(ndx));
                         }

                         mouseDownCoords = getMouseDownCoords();

                         delMouseX = mouseUpCoords[0] - mouseDownCoords[0];
                         delMouseY = mouseUpCoords[1] - mouseDownCoords[1];

                         delMouseXY[0] = ang2pixls*delMouseX;  delMouseXY[1] = ang2pixls*delMouseY; 
                    } 

///////////////
                    else
                    {
                         if (natoms > 0)
                         {
                    //          keyID = getKeyID();


                              String molName = "rotatedMolecule";
                              generateMol2File(molName);

                              globalRotationAxes = getGlobalRotationAxes();

//                              globalRotationAxes = orientGlobalAxes(globalRotationAxes, theta, phi, psi);
                         }
                    }
///////////////


               }

               draggedMouse = false;
               releasedMouse = false;

               mouseX = e.getX();  mouseY = e.getY();

               selectMode = 1;
               glcanvas.repaint();
          }

          public double[][] setRotatedFcnalGrpCoords(int selectedName)
          {
               int targetIndx, bondedIndx, bondedBVNdx, bvndx, bvndx1, bvndx2;
               int nnatoms, nbvs, nfragbvs, fragID, targetBV, bondedBV, selectedBvNdx, selectedFragbvNdx, bv;
               int nsbs, ndbs, ntbs, nbondedAtombv, sbNdx, dbNdx, tbNdx;
               int[][] tmpConnectivity;
               double tx, ty, tz, bx, by, bz, bvx, bvy, bvz, Dt, Db, Dbv, currentDihedralAngle, finalDihedralAngle, delAngl, cosangle, sinangle;
               double r11, r22, r33, n11, n22, n33, Dr, Dd, bx_star, by_star, bz_star;
               double fgbdx, fgbdy, fgbdz, Dbd, fgbduvx, fgbduvy, fgbduvz, bv2x, bv2y, bv2z;
               double[] fgbd;
               double[][] fgbduv;
               double[][] rotMatrix;
               double[][] fragatomColors;
               double[][] tmpCoords;
               double[][] tmpBV;

               float[][] elementRGB;
               String symbol = new String();
               boolean parallelVecs, foundTargetAtomBond, foundAddedAtomBond, autoAdjustDihedral;
 
               rotMatrix = new double[ncoords][ncoords];

               // Initialize components of unit bond vectors
               tx = ty = tz = bx = by = bz = 0.0;

               bondedBVNdx = bondedBV = -1;

               // Get unit bond vector of target atom
               selectedBvNdx = selectedFragbvNdx = (-1);

               for (ndx = 0; ndx < bondVectors.length; ndx++)
               {
                    if (bondVectors[ndx][0] == selectedName)
                    {
                         selectedBvNdx = ndx;

                         targetAtomNdx = ( -((int)(selectedName/100) + 1) );

                         tx = bondVectors[ndx][1];
                         ty = bondVectors[ndx][2];
                         tz = bondVectors[ndx][3];

                         bondOrder = (int)bondVectors[ndx][4];

                         break;
                    }
               }
 
               symbol = elemID.get(targetAtomNdx);

               targetIndx = bondedIndx = 0;

               for (ndx = 0; ndx < atomicSymbol.length; ndx++)
               {
                    if (atomicSymbol[ndx].equals(symbol))
                    {
                         targetIndx = ndx;

                         break;
                    }
               }

               bondVectors = getBondVectors();
               nbvRows = bondVectors.length;

               compatBndOrdr = true;

               bondedIndx = elemNdx;

               nbondvectors = getNbondVectors();
               nfragbvs = fragbondVectors.length;

               bondedBVNdx = 0; 

               for (ndx = 0; ndx < nfragbvs; ndx++)
               {
                    if (((int)fragbondVectors[ndx][4]) == bondOrder)
                    {
                         compatBndOrdr = true;

                         // Get unit bond vector associated with functional group being added
                         bx = fragbondVectors[ndx][1];
                         by = fragbondVectors[ndx][2];
                         bz = fragbondVectors[ndx][3];

                         selectedFragbvIndex = ndx;
                         bondedAtomNdx = ( -((int)(fragbondVectors[ndx][0]/100) + 1) );

                         symbol = fragElemID.get(bondedAtomNdx);

                         for (k = 0; k < atomicSymbol.length; k++)
                         {
                              if (atomicSymbol[k].equals(symbol))
                              {
                                   bondedIndx = k;

                                   break;
                              }
                         }

                         break;
                    }
                    else
                    {
                         compatBndOrdr = false;
                    }
               }

               if (compatBndOrdr)
               {
                    fgbd = new double[nfragatoms];
                    fgbduv = new double[nfragatoms][ncoords];

                    // Compute fragment bond unit vectors
                    for (j = 0; j < nfragatoms; j++)
                    {
                         fgbdx = fragatomCoordinates[j][1] - fragatomCoordinates[bondedAtomNdx][1];
                         fgbdy = fragatomCoordinates[j][2] - fragatomCoordinates[bondedAtomNdx][2];
                         fgbdz = fragatomCoordinates[j][3] - fragatomCoordinates[bondedAtomNdx][3];

                         fgbd[j] = Math.sqrt( (fgbdx*fgbdx) + (fgbdy*fgbdy) + (fgbdz*fgbdz) );

                         if (fgbd[j] > 0)
                         { 
                              fgbduv[j][0] = fgbdx/fgbd[j];  fgbduv[j][1] = fgbdy/fgbd[j];  fgbduv[j][2] = fgbdz/fgbd[j];
                         }
                         else
                         {
                              fgbduv[j][0] = 0.0;  fgbduv[j][2] = 0.0;  fgbduv[j][2] = 0.0;
                         }
                    }

                    // Compute cross product [(-rt) x rb] ; // Note: positive angle rotates rb towards (-rt)

                    // Get unit vector antiparallel to 'rt'
                    tx = -tx;  ty = -ty;  tz = -tz;

                    r11 = ty*bz - tz*by;  r22 = tz*bx - tx*bz; r33 = tx*by - ty*bx;

                    Dr = Math.sqrt(r11*r11 + r22*r22 + r33*r33);

                    // Get cosine of angle of rotation to bring rb || (-rt)
                    cosangle = (tx*bx + ty*by + tz*bz);

                    parallelVecs = false;

                    if (Dr < 0.000010)
                    {      
                         parallelVecs = true;

                         if (cosangle < 0)
                         {
                              for (ndx = 0; ndx < nfragbvs; ndx++)
                              {
                                   fragbondVectors[ndx][1] = (-fragbondVectors[ndx][1]);
                                   fragbondVectors[ndx][2] = (-fragbondVectors[ndx][2]);
                                   fragbondVectors[ndx][3] = (-fragbondVectors[ndx][3]);
                              }

                              for (ndx = 0; ndx < nfragatoms; ndx++)
                              {
                                   fragatomCoordinates[ndx][1] = fragatomCoordinates[bondedAtomNdx][1] - (fragatomCoordinates[ndx][1] - fragatomCoordinates[bondedAtomNdx][1]);
                                   fragatomCoordinates[ndx][2] = fragatomCoordinates[bondedAtomNdx][2] - (fragatomCoordinates[ndx][2] - fragatomCoordinates[bondedAtomNdx][2]);
                                   fragatomCoordinates[ndx][3] = fragatomCoordinates[bondedAtomNdx][3] - (fragatomCoordinates[ndx][3] - fragatomCoordinates[bondedAtomNdx][3]);

                                   fragatomCoordinates[bondedAtomNdx][1] = atomCoordinates[targetAtomNdx][1];
                                   fragatomCoordinates[bondedAtomNdx][2] = atomCoordinates[targetAtomNdx][2];
                                   fragatomCoordinates[bondedAtomNdx][3] = atomCoordinates[targetAtomNdx][3];
                              }
                         }
                    }
                    else
                    {
                         fragatomCoordinates[bondedAtomNdx][1] = atomCoordinates[targetAtomNdx][1];
                         fragatomCoordinates[bondedAtomNdx][2] = atomCoordinates[targetAtomNdx][2];
                         fragatomCoordinates[bondedAtomNdx][3] = atomCoordinates[targetAtomNdx][3];

                         // Get unit vector along axis of rotation
                         Dr = Math.sqrt(r11*r11 + r22*r22 + r33*r33);
 
                         n11 = r11/Dr;  n22 = r22/Dr;  n33 = r33/Dr;
  
                         // Get sine of angle of rotation that brings rb || (-rt)
                         sinangle = Math.sin(Math.acos(cosangle));
                                             
                         // Rotate bond vectors about bonded atom
                         rotMatrix[0][0] = (n11 * n11) + (cosangle * (1 - n11 * n11));
                         rotMatrix[0][1] = ((n11 * n22) * (1 - cosangle)) + (n33 * sinangle);
                         rotMatrix[0][2] = ((n33 * n11) * (1 - cosangle)) - (n22 * sinangle);

                         rotMatrix[1][0] = ((n11 * n22) * (1 - cosangle)) - (n33 * sinangle);
                         rotMatrix[1][1] = (n22 * n22) + (cosangle * (1 - n22 * n22));
                         rotMatrix[1][2] = ((n22 * n33) * (1 - cosangle)) + (n11 * sinangle);
 
                         rotMatrix[2][0] = ((n33 * n11) * (1 - cosangle)) + (n22 * sinangle);
                         rotMatrix[2][1] = ((n22 * n33) * (1 - cosangle)) - (n11 * sinangle);
                         rotMatrix[2][2] = (n33 * n33) + (cosangle * (1 - n33 * n33));

                         // Compute rotated bond vector coordinates
                         for (ndx = 0; ndx < nfragbvs; ndx++)
                         {
                              bx = fragbondVectors[ndx][1];  by = fragbondVectors[ndx][2];  bz = fragbondVectors[ndx][3];
                                                                                
                              bx_star = rotMatrix[0][0] * bx + rotMatrix[0][1] * by + rotMatrix[0][2] * bz;  
                              by_star = rotMatrix[1][0] * bx + rotMatrix[1][1] * by + rotMatrix[1][2] * bz;  
                              bz_star = rotMatrix[2][0] * bx + rotMatrix[2][1] * by + rotMatrix[2][2] * bz; 

                              fragbondVectors[ndx][1] = bx_star;
                              fragbondVectors[ndx][2] = by_star;
                              fragbondVectors[ndx][3] = bz_star;
                         }
 
                         bondLngth = getDefaultBondLength(targetIndx, bondedIndx, bondOrder);

                         fragatomCoordinates[bondedAtomNdx][1] -= tx*bondLngth;
                         fragatomCoordinates[bondedAtomNdx][2] -= ty*bondLngth;
                         fragatomCoordinates[bondedAtomNdx][3] -= tz*bondLngth;

                         for (ndx = 0; ndx < nfragatoms; ndx++)
                         {
                              // Rotate fragment bond unit vectors
                              fgbduvx = rotMatrix[0][0] * fgbduv[ndx][0] + rotMatrix[0][1] * fgbduv[ndx][1] + rotMatrix[0][2] * fgbduv[ndx][2];
                              fgbduvy = rotMatrix[1][0] * fgbduv[ndx][0] + rotMatrix[1][1] * fgbduv[ndx][1] + rotMatrix[1][2] * fgbduv[ndx][2];
                              fgbduvz = rotMatrix[2][0] * fgbduv[ndx][0] + rotMatrix[2][1] * fgbduv[ndx][1] + rotMatrix[2][2] * fgbduv[ndx][2];

                              fragatomCoordinates[ndx][1] = fragatomCoordinates[bondedAtomNdx][1] + fgbduvx*fgbd[ndx];
                              fragatomCoordinates[ndx][2] = fragatomCoordinates[bondedAtomNdx][2] + fgbduvy*fgbd[ndx];
                              fragatomCoordinates[ndx][3] = fragatomCoordinates[bondedAtomNdx][3] + fgbduvz*fgbd[ndx];

                              // Set indices of functional group atoms
                              fragatomCoordinates[ndx][0] = ndx + natoms; 

                              // Set fragmentID of added functional group atoms
                              fragatomCoordinates[ndx][4] = atomCoordinates[targetAtomNdx][4];
                         }
                    }
               }

               return fragatomCoordinates;
          }

          public void mouseClicked(MouseEvent e)
          {
               int numMouseclicks, nmouseclicks;
               long delayTime, delTime;
     
               Calendar now = Calendar.getInstance();

               delayTime = 400;
               delTime = delayTime;            
   
               numMouseclicks = e.getClickCount(); 

               if (numMouseclicks > 2)
               {
                    if ((numMouseclicks % 2) == 1)
                    {
                         numMouseclicks = 1;
                    }
                    else if ((numMouseclicks % 2) == 0)
                    {
                         numMouseclicks = 2;
                    }
               }

               if (numMouseclicks == 1)
               {
                    if (timerRunning == false)
                    {
                         timer = new java.util.Timer();   
                         timer.schedule(new stopTimer(), delayTime);

                         timerRunning = true;

                         msec1 = now.getTimeInMillis();
                  }
               }
               else if (numMouseclicks == 2)
               {
                    if (timerRunning)
                    {
                         timer.cancel();
                         timerRunning = false;
                      
                         msec2 = now.getTimeInMillis();

                         delTime = msec2 - msec1;
                    }
               }

               if (delTime < delayTime)
               {
                    nmouseClicks = 2;
               }
               else
               {
                    nmouseClicks = 1;
               }               

               hidePopupMenus();
               hidePopupSubmenus();

              // Left mouse button clicked 
               if (((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK))
               {
                    mouseButtonClicked = 1;

                    combScaleFactor = molScaleFactor * canvasScaleFactor;

                    natoms = getNatoms();

                    if (numMouseclicks == 1)
                    {                    
                         rotType = 0;

                         strBondAngle = ""; strDihedralAngle = "";

idCentralDhBond = false;

                         selectedNames = getSelectedNames();
                         nameSelected = getNameSelected();
                         selectedName = getSelectedName();

                         glcanvas.repaint();

                    }

                    if (numMouseclicks == 2)
                    {
                         int nnatoms, nbvs, nfragbvs, selectedFragbvNdx;
                         int[][] tmpConnectivity;
                         double delMouseX, delMouseY;
                         double[][] tmpCoords; 
                         double[][] tmpBV;
                         boolean compatBndOrdr;
                         String symbol = new String();
                         String hbType = new String();
                         String molName, solvAtomInputFile;
                         File solvAtomFile;

                         selectedNames = getSelectedNames();
                         selectedName = getSelectedName();
                         nameSelected = getNameSelected();

                         clearDataFields();

                         natoms = getNatoms();
                         
                         theta = getTheta();  phi = getPhi();  psi = getPsi();

                         idCentralDhBond = false;   // Delete ring identgifying central bond of selected dihedral

                         if (addSolventH2O)
                         {
                              if (selectedNames.size() == 1)
                              {
                                   nameSelected = selectedNames.get(0);

                                   if ( (nameSelected >= 0) && (nameSelected < natoms) )
                                   {
                                        int nnNdx, nnnNdx, nbondedAtoms, nhbAxes, targetNdx;
                                        double dx1, dy1, dz1, dx2, dy2, dz2;
                                        double dx, dy, dz, r1, r2, r3, R;

                                        double[][] hbAxis;

                                        symbol = elemID.get(nameSelected);

                                        hbDonor = hbAcceptor = false;

                                        // Determine if selected atom is an H-bond donor
                                        if (symbol.equals("H"))
                                        {
//                                             double[][] donorAxis;
//                                             double[][] hbAxis;
                                             String hdonorNN = new String();

                                             hbDonorNdx = nameSelected;
                                             hbDonorID = symbol;

                                             nnNdx = nnnNdx = (-1);

                                             // Get atom bonded to H-bond donor atom
                                             for (ndx = 0; ndx < natoms; ndx++)
                                             {
                                                  if (atomConnectivity[hbDonorNdx][ndx] == 1)
                                                  {
                                                       nnNdx = ndx;

                                                       // Get ID of atom bonded to H-bond donor atom
                                                       hdonorNN = elemID.get(nnNdx);

                                                       break;
                                                  }
                                             }

                                             if (Hacceptors.contains(hdonorNN))
                                             { 
                                                  hbDonor = true;
                                                  hbType = "Hdonor";
                                             }
                                             else if (hdonorNN.equals("C"))
                                             {
                                                  // Determine if carbon atom nearest-neighbor is part of a ring system (a ring carbon)
                                                  boolean ringCarbon, electronegativeNeighbor;

                                                  ringCarbon = electronegativeNeighbor = false;

                                                  ringCarbon = isRingCarbon(hbDonorNdx, nnNdx);

                                                  // If carbon atom nearest-neighbor is not a ring carbon (ringCXarbon = false), determine
                                                  //   if carbon atom nearest-neighbor or next nearest-neighbor is an electronegative atom
                                                  if (!ringCarbon)
                                                  {
                                                       electronegativeNeighbor = hasElectronegativeNeighbor(hbDonorNdx, nnNdx);
                                                  }

                                                  if ( (ringCarbon) || (electronegativeNeighbor) )
                                                  {
                                                       hbDonor = true;
                                                       hbType = "Hdonor";
                                                  }
                                             }

                                             if (hbDonor)
                                             {
                                                  nhbAxes = 1;

                                                  // Compute H-bond donor axis direction
                                                  dx =  atomCoordinates[hbDonorNdx][1] - atomCoordinates[nnNdx][1];               
                                                  dy =  atomCoordinates[hbDonorNdx][2] - atomCoordinates[nnNdx][2];               
                                                  dz =  atomCoordinates[hbDonorNdx][3] - atomCoordinates[nnNdx][3];       

                                                  R = Math.sqrt( (dx*dx) + (dy*dy) + (dz*dz) );

                                                  hbAxis = new double[nhbAxes][ncoords];
                                                  hbAxis[0][0] = dx/R;  hbAxis[0][1] = dy/R;  hbAxis[0][2] = dz/R;  

                                                  // Get index of any atom bonded to the atom bonded to the H-bond donor atom
                                                  for (ndx = 0; ndx < natoms; ndx++)
                                                  {
                                                       if ( (atomConnectivity[nnNdx][ndx] > 0) && (ndx != hbDonorNdx) )
                                                       {
                                                            nnnNdx = ndx;

                                                            break;
                                                       }
                                                  }    

                                                  if (hbDonorNdx == currentlySelectedAtomNdx)
                                                  {
                                                       solventOrientNum++;

                                                       if (solventOrientNum > 3)
                                                       {
                                                            solventOrientNum = 1;
                                                       }

                                                       molName = moleculeName + "_s" + atomNames.get(hbDonorNdx) + "_" + Integer.toString(solventOrientNum);

//                                                       solvAtomFile = new File(strSolvatedMoleculesDir + "\\" + moleculeName);
//                                                       solvAtomInputFile = solvAtomFile + "\\" + molName + ".mol2";

                                                       solvAtomFile = new File(strSolvatedMoleculesDir + fsep + moleculeName);
                                                       solvAtomInputFile = solvAtomFile + fsep + molName + ".mol2";

                                                       if (solvAtomFile.exists())
                                                       {
                                                            try
                                                            {
                                                                 readCoordsFile(solvAtomInputFile);

                                                                 glcanvas.repaint();
                                                            }
                                                            catch (IOException e1)
                                                            {
                                                                 e1.printStackTrace();
                                                            }
                                                       }
                                                  }
                                                  else
                                                  {
                                                       String  strSolvAtomFile;
                                                       String strSolvatedAtomFile;
                                                       File solvatedAtomFile;

                                                       solventOrientNum = 1;

                                                       molName = moleculeName + "_s" + atomNames.get(hbDonorNdx) + "_" + Integer.toString(solventOrientNum);
//                                                       solvAtomFile = new File(strSolvatedMoleculesDir + "\\" + moleculeName);
                                                       solvAtomFile = new File(strSolvatedMoleculesDir + fsep + moleculeName);

//                                                       solvAtomInputFile = solvAtomFile + "\\" + molName + ".mol2";
                                                       solvAtomInputFile = solvAtomFile + fsep + molName + ".mol2"; 
                                                      solvatedAtomFile = new File(solvAtomInputFile);

                                                       currentlySelectedAtomNdx = hbDonorNdx;

                                                       if (solvatedAtomFile.exists())
                                                       {
                                                            try
                                                            {
                                                                 readCoordsFile(solvAtomInputFile);

                                                                 glcanvas.repaint();
                                                            }
                                                            catch (IOException e2)                    
                                                            {
                                                                 e2.printStackTrace();
                                                            }

                                                            JOptionPane.showMessageDialog(glcanvas, "Note: This atom has already been solvated!", "Previously solvated atom selected", JOptionPane.PLAIN_MESSAGE);
                                                       }    
                                                       else
                                                       {    
                                                            solventOrientNum = 0;

                                                            // Call function to solvate H-bond donor atom
                                                            solvateHdonor(hbAxis, hbDonorNdx, nnNdx, nnnNdx); 

                                                            glcanvas.repaint();
                                                       }
                                                  }
                                             }
                                        }
                                        else if (Hacceptors.contains(symbol))
                                        {
                                             int hbAcceptorNdx, bonded2AcceptorNdx, nacceptorAxes, nhbrefAxes;
                                             int nsbs, ndbs, ntbs;
                          
                                             ArrayList<Integer> hAcceptorPartners = new ArrayList<Integer>();
                                             ArrayList<Integer> hAcceptorNNIndices = new ArrayList<Integer>();

                                             double[] hbrefAxis;
                                             double[][] acceptorAxes;

                                             hbAcceptorNdx = nameSelected;
                                             hbAcceptorID = symbol;

                                             nnNdx = nnnNdx = (-1);

                                             hbAcceptor = true;
                                             hbType = "Hacceptor";

                                             nhbrefAxes = 1;

                                             hbrefAxis = new double[ncoords];

                                             // Get index list of atoms bonded to H-acceptor atom
                                             for (ndx = 0; ndx < natoms; ndx++)
                                             {
                                                  if (atomConnectivity[hbAcceptorNdx][ndx] > 0)
                                                  {
                                                       hAcceptorNNIndices.add(ndx);
                                                  }
                                             }

                                             if (hAcceptorNNIndices.size() == 1)
                                             {                                            
                                                  nhbAxes = 1;

                                                  nnNdx = hAcceptorNNIndices.get(0);

                                                  dx = atomCoordinates[hbAcceptorNdx][1] - atomCoordinates[nnNdx][1];
                                                  dy = atomCoordinates[hbAcceptorNdx][2] - atomCoordinates[nnNdx][2];
                                                  dz = atomCoordinates[hbAcceptorNdx][3] - atomCoordinates[nnNdx][3];

                                                  R = Math.sqrt(dx*dx + dy*dy + dz*dz);

                                                  hbAxis = new double[nhbAxes][ncoords];
                                                  hbAxis[0][0] = dx/R;  hbAxis[0][1] = dy/R;  hbAxis[0][2] = dz/R;

                                             }
                                             else if (hAcceptorNNIndices.size() == 2)
                                             {
                                                  if (hbAcceptorID.equals("N"))
                                                  {
                                                       nhbAxes = 1;

                                                       nnNdx = hAcceptorNNIndices.get(0);

                                                       dx1 = atomCoordinates[hbAcceptorNdx][1] - atomCoordinates[nnNdx][1];
                                                       dy1 = atomCoordinates[hbAcceptorNdx][2] - atomCoordinates[nnNdx][2];
                                                       dz1 = atomCoordinates[hbAcceptorNdx][3] - atomCoordinates[nnNdx][3];

                                                       R = Math.sqrt(dx1*dx1 + dy1*dy1 + dz1*dz1);

                                                       dx1 = dx1/R;  dy1 = dy1/R;  dz1 = dz1/R; 

                                                       nnNdx = hAcceptorNNIndices.get(1);

                                                       dx2 = atomCoordinates[hbAcceptorNdx][1] - atomCoordinates[nnNdx][1];
                                                       dy2 = atomCoordinates[hbAcceptorNdx][2] - atomCoordinates[nnNdx][2];
                                                       dz2 = atomCoordinates[hbAcceptorNdx][3] - atomCoordinates[nnNdx][3];

                                                       R = Math.sqrt(dx2*dx2 + dy2*dy2 + dz2*dz2);

                                                       dx1 = dx2/R;  dy2 = dy2/R;  dz2 = dz2/R; 
                                             
                                                       dx = dx1 + dx2;  dy = dy1 + dy2;  dz = dz1 + dz2;
         
                                                       R = Math.sqrt(dx*dx + dy*dy + dz*dz);

                                                       hbAxis = new double[nhbAxes][ncoords];
                                                       hbAxis[0][0] = dx/R;  hbAxis[0][1] = dy/R;  hbAxis[0][2] = dz/R;
    
                                                       // Get reference axis on molecule for orienting OH bond on solvent H2O molecule
                                                       r1 = dy1*dz2 - dy2*dz1;  r2 = dz1*dx2 - dz2*dx1;  r3 = dx1*dy2 - dx2*dy1;

                                                       R = Math.sqrt(r1*r1 + r2*r2 + r3*r3);

                                                       hbrefAxis[0] = r1/R;  hbrefAxis[1] = r2/R;  hbrefAxis[2] = r3/R;

                                                       // Call function to solvate H-bond donor atom
                                                       solvateHacceptor(hbAxis, hbAcceptorNdx, hbrefAxis); 
                                                  }
                                                  else if (hbAcceptorID.equals("O"))
                                                  {
                                                       nhbAxes = 3;  

                                                  }


                                             }

                                             // Call function to solvate H-bond donor atom
//                                             solvateHacceptor(hbAxis, hbAcceptorNdx, hbrefAxis); 




/*

public void solvateHacceptor(hbAxis, hbAcceptorNdx, hbrefAxis); 

*/


/*

                                             // Get number of atoms bonded to H-bond acceptor atom along
                                             //     with index of each atom bonded to H-bond acceptor
                                             nhbAxes = 0;
                                             nacceptorAxes = 0;
                                             bonded2AcceptorNdx = -1;

                                             for (ndx = 0; ndx < natoms; ndx++)
                                             {
                                                  if (atomConnectivity[hbAcceptorNdx][ndx] > 0)
                                                  {                                             
                                                       nbondedAtoms++;

                                                       hAcceptorPartners.add(ndx);

                                                       // Determine numeber off single, double, and triple bonds 


                                                  }
                                             }

                                             if (symbol.equals("O"))
                                             {
                                                  if (nbondedAtoms == 1)     // O atom has 1 double bond
                                                  {
                                                       nacceptorAxes = 1;

                                                       // Get single atom bonded to H-bond acceptor 
                                                       bonded2AcceptorNdx = hacceptorPartners.get(0);

                                                       // Get direction of R=O double bond
                                                       dx =  atomCoordinates[hbAcceptorNdx][1] - atomCoordinates[bonded2AcceptorNdx][1];               
                                                       dy =  atomCoordinates[hbAcceptorNdx][2] - atomCoordinates[bonded2AcceptorNdx][2];               
                                                       dz =  atomCoordinates[hbAcceptorNdx][3] - atomCoordinates[bonded2AcceptorNdx][3];               

                                                       R = Math.sqrt( (dx*dx) + (dy*dy) + (dz*dz) );
  
                                                       acceptorAxes = new double[nacceptorAxes][ncoords];
                                                       acceptorAxes[0][0] = dx/R;  acceptorAxes[0][1] = dy/R;  acceptorAxes[0][2] = dz/R;
                                                  }
                                                  else if (nbondedAtoms == 2)
                                                  {
                                                       //
                                                  }
                                             }

                                             // Call solvateAtom function
*/

                                        }
                                   }
                              }
                         }
                         else if ( (addFcnalGrp) || (addFragment) )
                         {
                              if (selectedNames.size() == 0)
                              {
                                   if ( (deleteKeyDown) && (natoms == nfragatoms) )
                                   {
                                        eraseMolecule = getClearDisplayStatus();      

                                        if (eraseMolecule)
                                        {                    
                                             clearDisplay();
                                        }
                                   }
                                   else if (altKeyDown)
                                   {
                                        // Get atom indices with max and min x and y coordinates of functional group/fragment
                                        int maxfragxNdx, minfragxNdx, maxfragyNdx, minfragyNdx, fragIndx, maxfragID;
                                        int ndx1, ndx2;
                                        double X, Y, Z, THETA, PHI, PSI;
                                        double frag_maxx, frag_minx, frag_maxy, frag_miny;
                                        double maxx, minx, maxy, miny, maxz, minz;
                                        double maxX, maxY,maxZ, minX, minY, minZ;
                                        boolean addNewFragment;

                                        THETA = getTheta(); PHI = getPhi(); PSI = getPsi();

                                        frag_maxx = frag_minx = fragatomCoordinates[0][1];
                                        frag_maxy = frag_miny = fragatomCoordinates[0][2];

                                        maxfragxNdx = minfragxNdx = maxfragyNdx = minfragyNdx = fragIndx = 0;

                                        nfragatoms = fragatomCoordinates.length;

                                        for (ndx = 1; ndx < nfragatoms; ndx++)
                                        {
                                             if (fragatomCoordinates[ndx][1] > frag_maxx)
                                             {
                                                  frag_maxx = fragatomCoordinates[ndx][1];
                                                  maxfragxNdx = ndx;
                                             }
                                             else if (fragatomCoordinates[ndx][1] < frag_minx)
                                             {
                                                  frag_minx = fragatomCoordinates[ndx][1];
                                                  minfragxNdx = ndx;
                                             }

                                             if (fragatomCoordinates[ndx][2] > frag_maxy)
                                             {
                                                  frag_maxy = fragatomCoordinates[ndx][2];
                                                  maxfragyNdx = ndx;
                                             }
                                             else if (fragatomCoordinates[ndx][1] < frag_miny)
                                             {
                                                  frag_miny = fragatomCoordinates[ndx][2];
                                                  minfragyNdx = ndx;
                                             }
                                        }

                                        // Get atom indices with max and min x and y coordinates of existing molecule
                                        maxx = minx = atomCoordinates[0][1];
                                        maxy = miny = atomCoordinates[0][2];
                                        maxz = minz = atomCoordinates[0][3];

                                        for (ndx = 1; ndx < natoms; ndx++)
                                        {
                                             maxx = Math.max(maxx, atomCoordinates[ndx][1]);
                                             minx = Math.min(minx, atomCoordinates[ndx][1]);

                                             maxy = Math.max(maxy, atomCoordinates[ndx][2]);
                                             miny = Math.min(miny, atomCoordinates[ndx][2]);

                                             maxz = Math.max(maxz, atomCoordinates[ndx][3]);
                                             minz = Math.min(minz, atomCoordinates[ndx][3]);
                                        }

                                        // Get mouse coordinates
                                        mouseX = e.getX();  mouseY = e.getY();  mouseZ = 0.0;
 
                                        mouseX = (ang2pixls/combScaleFactor)*(mouseX - ((double)panelWidth/2.0));
                                        mouseY = (ang2pixls/combScaleFactor)*(((double)panelHeight/2.0) - mouseY);

                                        // Compensate for rotations about y-axis

                                        // mouse coordinates
                                        X = (mouseX*Math.cos((Math.PI/180)*THETA) - mouseZ*Math.sin((Math.PI/180)*THETA));
                                        Y = mouseY;
                                        Z = (mouseX*Math.sin((Math.PI/180)*THETA) + mouseZ*Math.cos((Math.PI/180)*THETA));

                                        // maximum and minimum atom coordinates
                                        maxx = maxx/combScaleFactor;  minx = minx/combScaleFactor;
                                        maxy = maxy/combScaleFactor;  miny = miny/combScaleFactor;

                                        maxX = (maxx*Math.cos((Math.PI/180)*THETA) - maxz*Math.sin((Math.PI/180)*THETA));
                                        maxY = maxy;
                                        maxZ = (maxx*Math.sin((Math.PI/180)*THETA) + maxz*Math.cos((Math.PI/180)*THETA));

                                        minX = (minx*Math.cos((Math.PI/180)*THETA) - minz*Math.sin((Math.PI/180)*THETA));
                                        minY = miny;
                                        minZ = (minx*Math.sin((Math.PI/180)*THETA) + minz*Math.cos((Math.PI/180)*THETA));

                                        // Compensate for rotations about x-axis

                                        // mouse coordinates
                                        coordx = X;
                                        coordy = (Y*Math.cos((Math.PI/180)*PHI) - Z*Math.sin((Math.PI/180)*PHI));
                                        coordz = (Y*Math.sin((Math.PI/180)*PHI) + Z*Math.cos((Math.PI/180)*PHI));

                                        // maximum and minimum atom coordinates
                                        maxx = maxX;
                                        maxy = (maxY*Math.cos((Math.PI/180)*PHI) - maxZ*Math.sin((Math.PI/180)*PHI));
                                        maxz = (maxY*Math.sin((Math.PI/180)*PHI) + maxZ*Math.cos((Math.PI/180)*PHI));

                                        minx = minX;
                                        miny = (minY*Math.cos((Math.PI/180)*PHI) - minZ*Math.sin((Math.PI/180)*PHI));
                                        minz = (minY*Math.sin((Math.PI/180)*PHI) + minZ*Math.cos((Math.PI/180)*PHI));

                                        // Compensate for rotations about z-axis

                                        // mouse coordinates
                                        Y = coordy; Z = coordz;

                                        coordx = (X*Math.cos((Math.PI/180)*PSI) - Y*Math.sin((Math.PI/180)*PSI));
                                        coordy = (X*Math.sin((Math.PI/180)*PSI) + Y*Math.cos((Math.PI/180)*PSI));

                                        // maximum and minimum atom coordinates
                                        maxY = maxy;  maxz = maxZ;  

                                        maxx = (maxX*Math.cos((Math.PI/180)*PSI) - maxY*Math.sin((Math.PI/180)*PSI));
                                        maxy = (maxX*Math.sin((Math.PI/180)*PSI) + maxY*Math.cos((Math.PI/180)*PSI));

                                        minY = miny;  minz = minZ; 

                                        minx = (minX*Math.cos((Math.PI/180)*PSI) - minY*Math.sin((Math.PI/180)*PSI));
                                        miny = (minX*Math.sin((Math.PI/180)*PSI) + minY*Math.cos((Math.PI/180)*PSI));

                                        addNewFragment = false;

                                        // Add fragment if mouse coordinates are outside the boundaries of an existing molecule
                                        if (coordx > maxx)
                                        {
                                             fragIndx = minfragxNdx;

                                             addNewFragment = true;
                                        }
                                        else if (coordx < maxx)
                                        {
                                             fragIndx = maxfragxNdx;

                                             addNewFragment = true;
                                        }
                                        else
                                        {
                                             if (coordy > maxy)
                                             {
                                                  fragIndx = minfragyNdx;

                                                  addNewFragment = true;
                                             }
                                             else if (coordy < maxy)
                                             {
                                                  fragIndx = maxfragyNdx;

                                                  addNewFragment = true;
                                             }
                                        }

                                        // Do not add fragment if mouse coordinates are within the boundaries of an existing molecule
                                        if ( (coordx <= maxx) && (coordx >= minx) && (coordy <= maxy) && (coordy >= miny) )
                                        {
                                             addNewFragment = false;
                                        }

                                        if (addNewFragment)
                                        {
                                             nsubstructures = getNsubstructures();

                                             nsubstructures++;

                                             nfragbvs = 0;

                                             // Get maximum fragment ID for existing molecule(s)
                                             maxfragID = 0;

                                             for (ndx = 0; ndx < natoms; ndx++)
                                             {
                                                  maxfragID = Math.max(maxfragID, (int)atomCoordinates[ndx][4]);
                                             }

                                             // Increment maxfragID by 1
                                             maxfragID++;

                                             for (ndx = 0; ndx < nfragatoms; ndx++)
                                             {
                                                  if (ndx != fragIndx)
                                                  {
                                                       fragatomCoordinates[ndx][1] = coordx - (fragatomCoordinates[ndx][1] - fragatomCoordinates[fragIndx][1]); 
                                                       fragatomCoordinates[ndx][2] = coordy - (fragatomCoordinates[ndx][2] - fragatomCoordinates[fragIndx][2]); 
                                                       fragatomCoordinates[ndx][3] = coordz - (fragatomCoordinates[ndx][3] - fragatomCoordinates[fragIndx][3]); 
                                                       fragatomCoordinates[ndx][4] = maxfragID;
                                                  }
                                             }

                                             fragatomCoordinates[fragIndx][1] = coordx;
                                             fragatomCoordinates[fragIndx][2] = coordy;
                                             fragatomCoordinates[fragIndx][3] = coordz;
                                             fragatomCoordinates[fragIndx][4] = maxfragID;

                                             for (ndx = 0; ndx < fragElemID.size(); ndx++)
                                             {
                                                  elemID.add(fragElemID.get(ndx));
                                             }

                                             atomNames.clear();
                                             setAtomNames(elemID);
                                             atomNames = getAtomNames();

                                             nnatoms = natoms + nfragatoms;

                                             AN = getAN(elemID, nnatoms);
                                             atomRadii = getAtomRadii(AN);

                                             elementRGB = new float[nnatoms][ncolors];
                                             setElementRGB(AN, nnatoms); 

                                             elementRGB = getElementColors();         

                                             // Add atom coordinates of functional group to those of target molecule
                                             tmpCoords = new double[natoms][ncoordCols];
 
                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < ncoordCols; k++)
                                                  {
                                                       tmpCoords[j][k] = atomCoordinates[j][k];
                                                  }
                                             }

                                             atomCoordinates = new double[nnatoms][ncoordCols];

                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < ncoordCols; k++)
                                                  {
                                                       atomCoordinates[j][k] = tmpCoords[j][k];
                                                  }
                                             } 

                                             for (j = 0; j < nfragatoms; j++)
                                             {
                                                  atomCoordinates[(natoms + j)][0] = natoms + j;

                                                  for (k = 1; k < ncoordCols; k++)
                                                  {
                                                       atomCoordinates[(natoms + j)][k] = fragatomCoordinates[j][k];
                                                  }
                                             }

                                             // Add atom connectivity of functional group to that of target molecule
                                             tmpConnectivity = new int[natoms][natoms];
                    
                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < natoms; k++)
                                                  { 
                                                       tmpConnectivity[j][k] = atomConnectivity[j][k];
                                                  }
                                             }

                                             atomConnectivity = new int[nnatoms][nnatoms];

                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < natoms; k++)
                                                  { 
                                                       atomConnectivity[j][k] = tmpConnectivity[j][k];

                                                       atomConnectivity[k][j] = atomConnectivity[j][k];
                                                  }
                                             }

                                             for (j = 0; j < nfragatoms; j++)
                                             {
                                                  ndx1 = natoms + j;

                                                  for (k = 0; k < nfragatoms; k++)
                                                  { 
                                                       ndx2 = natoms + k;

                                                       atomConnectivity[ndx1][ndx2] = fragatomConnectivity[j][k];

                                                       atomConnectivity[ndx2][ndx1] = atomConnectivity[ndx1][ndx2];
                                                  }
                                             }

                                             nbvs = bondVectors.length;

                                             tmpBV = new double[nbvs][nbvCols];
 
                                             for (j = 0; j < nbvs; j++)
                                             {
                                                  for (k = 0; k < nbvCols; k++)
                                                  {
                                                       tmpBV[j][k] =  bondVectors[j][k];
                                                  }
                                             }

                                             // If functional group is being added, get number of fragment bond vectors
                                             if (addFcnalGrp)
                                             {
                                                  nfragbvs = fragbondVectors.length;
                                             }

                                             nbondVectors = nbvs + nfragbvs;  

                                             bondVectors = new double[nbondVectors][nbvCols]; 

                                             for (j = 0; j < nbvs; j++)
                                             {
                                                  for (k = 0; k < nbvCols; k++)
                                                  {
                                                       bondVectors[j][k] = tmpBV[j][k];
                                                  }
                                             }

                                             for (j = 0; j < nfragbvs; j++)
                                             {
                                                  ndx = nbvs + j;        

                                                  // Reassign bond vector names to fragment bond vectors
                                                  bondVectors[ndx][0] = fragbondVectors[j][0] - 100*natoms;

                                                  for (k = 1; k < nbvCols; k++)
                                                  {
                                                       bondVectors[ndx][k] = fragbondVectors[j][k];
                                                  }
                                             }

                                             natoms = nnatoms;

                                             // Generate a *.mol2 file
//                                             generateMol2File("New Molecule");
                                             generateMol2File(moleculeName);

                                             displayStructure = true;

                                             selectedNames = getSelectedNames();

                                             selectMode = 1;
                                             glcanvas.repaint();
                                        }
                                   }
                              }
                              else if (selectedNames.size() == 1)
                              {
                                   selectedName = selectedNames.get(0);

                                   if ( (selectedName <= (-100)) && (addFcnalGrp) ) 
                                   {
                                        targetAtomNdx = ( -((int)(selectedName/100) + 1) );

                                        fragatomCoordinates = setRotatedFcnalGrpCoords(selectedName);

                                        nfragbvs = fragbondVectors.length;

                                        for (ndx = 0; ndx < fragElemID.size(); ndx++)
                                        {
                                             elemID.add(fragElemID.get(ndx));
                                        }

                                        compatBndOrdr = false;

                                        selectedFragbvNdx = (-1);
                                    
                                        compatBndOrdr = getCompatBndOrdr();

                                        if (compatBndOrdr)
                                        {
                                             atomNames.clear();
                                             setAtomNames(elemID);
                                             atomNames = getAtomNames();

                                             nnatoms = natoms + nfragatoms;

                                             AN = getAN(elemID, nnatoms);
                                             atomRadii = getAtomRadii(AN);

                                             elementRGB = new float[nnatoms][ncolors];
                                             setElementRGB(AN, nnatoms); 

                                             elementRGB = getElementColors();         

                                             // Add atom coordinates of functional group to those of target molecule
                                             tmpCoords = new double[natoms][ncoordCols];
 
                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < ncoordCols; k++)
                                                  {
                                                       tmpCoords[j][k] = atomCoordinates[j][k];
                                                  }
                                             }

                                             atomCoordinates = new double[nnatoms][ncoordCols];

                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < ncoordCols; k++)
                                                  {
                                                       atomCoordinates[j][k] = tmpCoords[j][k];
                                                  }
                                             } 

                                             for (j = 0; j < nfragatoms; j++)
                                             {
                                                  atomCoordinates[(natoms + j)][0] = natoms + j;

                                                  for (k = 1; k < ncoordCols; k++)
                                                  {
                                                       atomCoordinates[(natoms + j)][k] = fragatomCoordinates[j][k];
                                                  }
                                             }

                                             // Add atom connectivity of functional group to that of target molecule
                                             tmpConnectivity = new int[natoms][natoms];
                    
                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < natoms; k++)
                                                  { 
                                                       tmpConnectivity[j][k] = atomConnectivity[j][k];
                                                  }
                                             }

                                             atomConnectivity = new int[nnatoms][nnatoms];

                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < natoms; k++)
                                                  { 
                                                       atomConnectivity[j][k] = tmpConnectivity[j][k];
                                                  }
                                             }

                                             for (j = 0; j < nfragatoms; j++)
                                             {
                                                  for (k = 0; k < nfragatoms; k++)
                                                  { 
                                                       atomConnectivity[(j + natoms)][(k + natoms)] = fragatomConnectivity[j][k];
                                                       atomConnectivity[(k + natoms)][(j + natoms)] = atomConnectivity[(j + natoms)][(k + natoms)];
                                                  }
                                             }
      
                                             // Add bond between bonded atom and target atom
                                             atomConnectivity[targetAtomNdx][(bondedAtomNdx + natoms)] = atomConnectivity[(bondedAtomNdx + natoms)][targetAtomNdx] = bondOrder;
 
                                             // Remove bond vectors selected for bonding functional group and target molecule
                                             nbvs = bondVectors.length;

                                             tmpBV = new double[nbvs][nbvCols];
 
                                             for (j = 0; j < nbvs; j++)
                                             {
                                                  for (k = 0; k < nbvCols; k++)
                                                  {
                                                       tmpBV[j][k] =  bondVectors[j][k];
                                                  }
                                             }

                                             // Reduce number of bond vectors by the 2 bvs selected to create bond between functional group and target molecule
                                             nbondVectors = (nbvs - 1) + (nfragbvs - 1);  

                                             bondVectors = new double[nbondVectors][nbvCols]; 

                                             ndx = (-1);
 
                                             for (j = 0; j < nbvs; j++)
                                             {
                                                  if ( (int)tmpBV[j][0] != selectedName)
                                                  {
                                                       ndx++;

                                                       for (k = 0; k < nbvCols; k++)
                                                       {
                                                            bondVectors[ndx][k] = tmpBV[j][k];

                                                       }
                                                  }
                                             }

                                             for (j = 0; j < nfragbvs; j++)
                                             {
                                                  if (j != selectedFragbvIndex)
                                                  {
                                                       ndx++;

                                                       // Reassign bond vector names to fragment bond vectors
                                                       bondVectors[ndx][0] = fragbondVectors[j][0] - 100*natoms;

                                                       for (k = 1; k < nbvCols; k++)
                                                       {
                                                            bondVectors[ndx][k] = fragbondVectors[j][k];
                                                       }
                                                  }
                                             }

                                             natoms = nnatoms;

                                             // Generate a *.mol2 file
//                                             generateMol2File("New Molecule");
                                             generateMol2File(moleculeName);
 
                                             displayStructure = true;

                                             selectedNames = getSelectedNames();

                                             selectMode = 1;
                                             glcanvas.repaint();
                                        }
                                   }
                              }

                              selectMode = 1;
                              glcanvas.repaint();
                         }

                         if (selectedNames.size() == 0)
                         {
                              clearDataFields();

                              translateAtoms = getTranslateAtomsState();

                              if (translateAtoms)
                              {
                                   translatedAtomIndices = getTranslatedAtomIndices();

                                   delMouseXY = getDelMouseXY();

                                   atomCoordinates = getCoordinates();
                                   tmpCoords = getCoordinates();

                                   translateAtoms = false;

                                   glcanvas.repaint();
                              }

                              selectMode = 1;
                              glcanvas.repaint();
                         }  

                         if (selectedNames.size() == 1)
                         {
                              clearDataFields();

                              if ( (nameSelected >= 0) && (nameSelected < natoms) )
                              {
                                   bondVectors = getBondVectors();

                                   if (bondVectors.length > 0)
                                   {
                                        for (ndx = 0; ndx < bondVectors.length; ndx++)
                                        {
                                             if ( -((int)(bondVectors[ndx][0]/100) + 1) == nameSelected)
                                             {
                                                  if ( (int)(bondVectors[ndx][5]) == 1)
                                                  {
                                                       bondVectors[ndx][5] = 0;
                                                  }
                                                  else if ( (int)(bondVectors[ndx][5]) == 0)
                                                  {
                                                       bondVectors[ndx][5] = 1;
                                                  }
                                             }
                                        }
                                   }
                              }
                      
                              glcanvas.repaint();
                         }
                    }

                    if ( (!drawMode) || (addFragment) || (addFcnalGrp) )
                    { 
                         if ( (numMouseclicks == 2) && (deleteKeyDown) )
                         {
                              selectedNames = getSelectedNames();
                              nameSelected = getNameSelected();

                              selectedName = getSelectedName();

                              eraseMolecule = getClearDisplayStatus();      

                              if (eraseMolecule)
                              {                    
                                   clearDisplay();
                              }



//while(dhParametersTable.getRowCount() > 0)
//{
//     dhParametersTable.remove(0);
//}


                         }
                    }
                    else if (drawMode)
                    { 
                         if (numMouseclicks == 1)
                         {
                              clearDataFields();

                              strBondAngle = strDihedralAngle = "";

                              selectedNames = getSelectedNames();                                   

                              selectMode = 1;
                              glcanvas.repaint();

                         }
                         else if (numMouseclicks == 2)
                         {
                              int bondOrder = 0;
                              double X, Y, Z, THETA, PHI, PSI;

                              THETA = getTheta();  PHI = getPhi();  PSI = getPsi();

                              clearDataFields();

                              if (natoms == 0)
                              {
                                   File file = new File(mol2filename + ".mol2");
                                   file.delete();

                                   bvfilename = mol2filename + "bv.txt";
                                   file = new File(bvfilename);
                                   file.delete();

                                   atomTypes.clear();

                                   natoms++;  fragmentNum++;
                                   nbonds = nbvRows = 0;
                                   nsubstructures = 1;

                                   theta = phi = psi = 0.0;
                                   scaleFactor = molScaleFactor = canvasScaleFactor = 1.0;

                                   mouseX = e.getX();  mouseY = e.getY();
                                   coordx = ang2pixls*(mouseX - (double)panelWidth/2.0);
                                   coordy = ang2pixls*(((double)panelHeight/2.0) - mouseY);
                                   coordz = 0.0;

                                   atomCoordinates = new double[natoms][ncoordCols];
                                   atomConnectivity = new int[natoms][natoms];

                                   atomCoordinates[0][0] = 0;
                                   atomCoordinates[0][1] = coordx; atomCoordinates[0][2] = coordy; atomCoordinates[0][3] = coordz; 
                                   atomCoordinates[0][4] = 1;
                              
                                   atomConnectivity[0][0] = 0;

                                   elemID.clear();               

                                   selectedElem = getSelectedElem();
                                   elemID.add(selectedElem);

                                   atomNames.clear();
                                   setAtomNames(elemID);
                                   atomNames = getAtomNames();
   
                                   AN.clear();
                                   atomnumbr = getAtomNumbr();
                                   AN.add(atomnumbr);

                                   atomRadii = getAtomRadii(AN);

                                   setElementRGB(AN, natoms);

                                   elementRGB = new float[natoms][ncolors];
                                   elementRGB = getElementColors();

                                   elemNdx = (atomnumbr - 1);

                                   nbonds = 0;
                                   nbondvectors = getNbondVectors();
                                   
                                   newBondVectors = new double[nbondVectors][nnewbvCols];
                                   newBondVectors = getNewBondVectors(elemNdx, nbondVectors);

                                   bondVectors = new double[nbondvectors][nbvCols];

                                   bvID.clear();

                                   for (ndx = 0; ndx < nbondVectors; ndx++)
                                   {
                                        bondVectors[ndx][0] = -(100*natoms + ndx);
                                        bondVectors[ndx][1] = newBondVectors[ndx][0];
                                        bondVectors[ndx][2] = newBondVectors[ndx][1];
                                        bondVectors[ndx][3] = newBondVectors[ndx][2];
                                        bondVectors[ndx][4] = newBondVectors[ndx][3];
                                        bondVectors[ndx][5] = 1;

                                        bvID.add((int)bondVectors[ndx][0]);
                                   }

                                   generateMol2File(moleculeName);

                                   displayStructure = true;

                                   selectedNames = getSelectedNames();

                                   selectMode = 1;
                                   glcanvas.repaint();
                              }
                              else if (natoms > 0)
                              {
                                   int an1, an2;
                                   int[][] tmpConnectivity;
                                   double n11, n22, n33, mouseX0, mouseY0, mouseZ0, mouseZ;
                                   double[][] tmpCoords;
                                   double[][] tmpBondVectors;
                                   double[][] tmpBondLength;
                                   double[][] rotMatrix;
                                   float[][] tmpRGB;

                                   if (addElement)
                                   {                        
                                        selectedNames = getSelectedNames();
                                        nameSelected = getNameSelected();
                                        natoms = getNatoms();

                                        selectedElem = getSelectedElem();
 
                                        if ( (selectedNames.size() == 0) && (altKeyDown) )
                                        {
                                             elemID.add(selectedElem);
                                        }
                                        else if ( (selectedNames.size() == 1) && (selectedNames.get(0) <= (-100)) && (!altKeyDown) )
                                        {
                                             elemID.add(selectedElem);
                                        }

                                        atomNames.clear();
                                        setAtomNames(elemID);
                                        atomNames = getAtomNames();

                                        atomnumbr = getAtomNumbr();
                                        AN.add(atomnumbr);

                                        atomRadii = getAtomRadii(AN);

                                        newColor = getNewColor();

                                        tmpRGB = new float[natoms][ncolors];
                                        tmpRGB = getElementRGB();

                                        elementRGB = new float[(natoms+1)][ncolors];

                                        for (j = 0; j < natoms; j++)
                                        {
                                             for (k = 0; k < ncolors; k++)
                                             {
                                                  elementRGB[j][k] = tmpRGB[j][k];
                                             }
                                        }
                    
                                        for (k = 0; k < ncolors; k++)
                                        {
                                             elementRGB[natoms][k] = newColor.get(k);
                                        }

                                        elemNdx = (atomnumbr - 1);

                                        nbondvectors = getNbondVectors();

                                        selectMode = 1;
                                        glcanvas.repaint();

                                        tmpCoords = new double[natoms][ncoordCols];
    
                                        for (j = 0; j < natoms; j++)
                                        {
                                             for (k = 0; k < ncoordCols; k++)
                                             {
                                                  tmpCoords[j][k] = atomCoordinates[j][k];
                                             }
                                        }

                                        atomCoordinates = new double[(natoms+1)][ncoordCols];

                                        for (j = 0; j < natoms; j++)
                                        {
                                             for (k = 0; k < ncoordCols; k++)
                                             {
                                                  atomCoordinates[j][k] = tmpCoords[j][k];
                                             }
                                        }

                                        if ( (nameSelected == (-1)) && (altKeyDown) )
                                        {
                                             rotMatrix = new double[ncoords][ncoords];
  
                                             mouseX = e.getX();  mouseY = e.getY();  mouseZ = 0.0;
 
                                             mouseX = (ang2pixls/combScaleFactor)*(mouseX - ((double)panelWidth/2.0));
                                             mouseY = (ang2pixls/combScaleFactor)*(((double)panelHeight/2.0) - mouseY);

                                             // Compensate for rotations about y-axis
                                             X = (mouseX*Math.cos((Math.PI/180)*THETA) - mouseZ*Math.sin((Math.PI/180)*THETA));
                                             Y = mouseY;
                                             Z = (mouseX*Math.sin((Math.PI/180)*THETA) + mouseZ*Math.cos((Math.PI/180)*THETA));

                                             // Compensate for rotations about x-axis
                                             coordx = X;
                                             coordy = (Y*Math.cos((Math.PI/180)*PHI) - Z*Math.sin((Math.PI/180)*PHI));
                                             coordz = (Y*Math.sin((Math.PI/180)*PHI) + Z*Math.cos((Math.PI/180)*PHI));
  
                                             // Compensate for rotations about z-axis
                                             Y = coordy; Z = coordz;
  
                                             coordx = (X*Math.cos((Math.PI/180)*PSI) - Y*Math.sin((Math.PI/180)*PSI));
                                             coordy = (X*Math.sin((Math.PI/180)*PSI) + Y*Math.cos((Math.PI/180)*PSI));

                                             atomCoordinates[natoms][0] = natoms;
                                             atomCoordinates[natoms][1] = coordx;
                                             atomCoordinates[natoms][2] = coordy;
                                             atomCoordinates[natoms][3] = coordz;

                                             // Get maximum fragment ID value
                                             maxfragID = 1;
 
                                             for (ndx = 0; ndx < natoms; ndx++)
                                             {
                                                  maxfragID = Math.max(maxfragID, (int)atomCoordinates[ndx][4]);
                                             }
 
                                             atomCoordinates[natoms][4] = (maxfragID + 1);

                                             nsubstructures = getNsubstructures();

                                             nsubstructures++;

                                             fragmentNum++;

                                             bondVectors = getBondVectors();
                                             nbvRows = bondVectors.length;

                                             tmpBondVectors = new double[nbvRows][nbvCols];

                                             for (j = 0; j < nbvRows; j++)
                                             {
                                                  for (k = 0; k < nbvCols; k++)
                                                  {     
                                                       tmpBondVectors[j][k] = bondVectors[j][k];
                                                  }
                                             }

                                             nbondvectors = getNbondVectors();
                                             newBondVectors = new double[nbondVectors][nnewbvCols];
                                             newBondVectors = getNewBondVectors(elemNdx, nbondVectors);
 
                                             bondVectors = new double[(nbvRows + nbondvectors)][nbvCols];
 
                                             for (j = 0; j < nbvRows; j++)
                                             {
                                                  for (k = 0; k < nbvCols; k++)
                                                  {     
                                                       bondVectors[j][k] = tmpBondVectors[j][k];
                                                  }
                                             }

                                             for (ndx = 0; ndx < nbondVectors; ndx++)
                                             {
                                                  bondVectors[(nbvRows + ndx)][0] = -((100*(natoms+1)) + ndx);
                                                  bondVectors[(nbvRows + ndx)][1] = newBondVectors[ndx][0];
                                                  bondVectors[(nbvRows + ndx)][2] = newBondVectors[ndx][1];
                                                  bondVectors[(nbvRows + ndx)][3] = newBondVectors[ndx][2];
                                                  bondVectors[(nbvRows + ndx)][4] = newBondVectors[ndx][3];
                                                  bondVectors[(nbvRows + ndx)][5] = 1;

                                                  bvID.add((int)bondVectors[(nbvRows + ndx)][0]);
                                             }

                                             tmpConnectivity = getAtomConnectivity();
  
                                             atomConnectivity = new int[(natoms+1)][(natoms+1)];

                                             for (j = 0; j < natoms; j++)
                                             {
                                                  for (k = 0; k < natoms; k++)
                                                  {  
                                                       atomConnectivity[j][k] = tmpConnectivity[j][k];
                                                  }
                                             }

                                             natoms++;

//                                             generateMol2File("New Molecule");
                                             generateMol2File(moleculeName); 

                                             displayStructure = true;

                                             selectedNames = getSelectedNames();

                                             selectMode = 1;
                                             glcanvas.repaint();
                                        }
                                        else if ( (nameSelected <= (-100)) && (!altKeyDown) && (elementList.isVisible()) )
                                        {
                                             int targetIndx, bondedIndx, targetAtomNdx, bondedAtomNdx, bondedBVNdx, bvndx, bvndx1, bvndx2;
                                             int fragID, targetBV, bondedBV, bv;
                                             int nsbs, ndbs, ntbs, nbondedAtombv, sbNdx, dbNdx, tbNdx;
                                             double tx, ty, tz, bx, by, bz, bvx, bvy, bvz, Dt, Db, Dbv, currentDihedralAngle, finalDihedralAngle, delAngl, cosangle, sinangle;
                                             double r11, r22, r33, Dr, Dd, bx_star, by_star, bz_star;
                                             rotMatrix = new double[ncoords][ncoords];
                                             String symbol = new String();
                                             boolean parallelVecs, foundTargetAtomBond, foundAddedAtomBond, autoAdjustDihedral, compatBndOrdr;

                                             ArrayList<Double> axvec = new ArrayList<Double>();
                                             ArrayList<Double> bvvec1 = new ArrayList<Double>();
                                             ArrayList<Double> bvvec2 = new ArrayList<Double>();

                                             // Initialize components of unit bond vectors
                                             tx = ty = tz = bx = by = bz = 0.0;
 
                                             bondedBVNdx = bondedBV = -1;

                                             // Get unit bond vector of target atom
                                             targetAtomNdx = bondedAtomNdx = -1;

                                             for (ndx = 0; ndx < bondVectors.length; ndx++)
                                             {
                                                  if (bondVectors[ndx][0] == nameSelected)
                                                  {
                                                       targetAtomNdx = ( -((int)(nameSelected/100) + 1) );

                                                       tx = bondVectors[ndx][1];
                                                       ty = bondVectors[ndx][2];
                                                       tz = bondVectors[ndx][3];

                                                       bondOrder = (int)bondVectors[ndx][4];

                                                       break;
                                                  }
                                             }
 
                                             symbol = elemID.get(targetAtomNdx);

                                             targetIndx = bondedIndx = 0;

                                             for (ndx = 0; ndx < atomicSymbol.length; ndx++)
                                             {
                                                  if (atomicSymbol[ndx].equals(symbol))
                                                  {
                                                       targetIndx = ndx;

                                                       break;
                                                  }
                                             }

                                             bondVectors = getBondVectors();
                                             nbvRows = bondVectors.length;

                                             compatBndOrdr = true;

                                             bondedIndx = elemNdx;

                                             nbondvectors = getNbondVectors();

                                             newBondVectors = new double[nbondVectors][nnewbvCols];
                                             newBondVectors = getNewBondVectors(elemNdx, nbondVectors);

                                             for (ndx = 0; ndx < newBondVectors.length; ndx++)
                                             {
                                                  if ((int)newBondVectors[ndx][3] == bondOrder)
                                                  {
                                                       compatBndOrdr = true;

                                                       // Get unit bond vector of bonded atom
                                                       bx = newBondVectors[ndx][0];
                                                       by = newBondVectors[ndx][1];
                                                       bz = newBondVectors[ndx][2];

                                                       bondedBVNdx = ndx; 

                                                       break;
                                                  }
                                                  else
                                                  {
                                                       compatBndOrdr = false;
                                                  } 
                                             }
 
                                             if (compatBndOrdr)
                                             {
                                                  // Compute cross product [(-rt) x rb] ; // Note: positive angle rotates rb towards (-rt)

                                                  // Get unit vector antiparallel to 'rt'
                                                  tx = -tx;  ty = -ty;  tz = -tz;

                                                  r11 = ty*bz - tz*by;  r22 = tz*bx - tx*bz; r33 = tx*by - ty*bx;

                                                  Dr = Math.sqrt(r11*r11 + r22*r22 + r33*r33);

                                                  // Get cosine of angle of rotation to bring rb || (-rt)
                                                  cosangle = (tx*bx + ty*by + tz*bz);

                                                  parallelVecs = false;

                                                  if (Dr < 0.000010)
                                                  {      
                                                       parallelVecs = true;

                                                       if (cosangle < 0)
                                                       {
                                                            for (ndx = 0; ndx < newBondVectors.length; ndx++)
                                                            {
                                                                 newBondVectors[ndx][0] = (-newBondVectors[ndx][0]);
                                                                 newBondVectors[ndx][1] = (-newBondVectors[ndx][1]);
                                                                 newBondVectors[ndx][2] = (-newBondVectors[ndx][2]);
                                                            }
                                                       }
                                                  }
                                                  else
                                                  {
                                                       // Get unit vector along axis of rotation
                                                       Dr = Math.sqrt(r11*r11 + r22*r22 + r33*r33);
 
                                                       n11 = r11/Dr;  n22 = r22/Dr;  n33 = r33/Dr;
  
                                                       // Get sine of angle of rotation that brings rb || (-rt)
                                                       sinangle = Math.sin(Math.acos(cosangle));
                                             
                                                       // Rotate bond vectors about bonded atom
                                                       rotMatrix[0][0] = (n11 * n11) + (cosangle * (1 - n11 * n11));
                                                       rotMatrix[0][1] = ((n11 * n22) * (1 - cosangle)) + (n33 * sinangle);
                                                       rotMatrix[0][2] = ((n33 * n11) * (1 - cosangle)) - (n22 * sinangle);

                                                       rotMatrix[1][0] = ((n11 * n22) * (1 - cosangle)) - (n33 * sinangle);
                                                       rotMatrix[1][1] = (n22 * n22) + (cosangle * (1 - n22 * n22));
                                                       rotMatrix[1][2] = ((n22 * n33) * (1 - cosangle)) + (n11 * sinangle);
 
                                                       rotMatrix[2][0] = ((n33 * n11) * (1 - cosangle)) + (n22 * sinangle);
                                                       rotMatrix[2][1] = ((n22 * n33) * (1 - cosangle)) - (n11 * sinangle);
                                                       rotMatrix[2][2] = (n33 * n33) + (cosangle * (1 - n33 * n33));

                                                       // Compute rotated bond vector coordinates
                                                       for (ndx = 0; ndx < newBondVectors.length; ndx++)
                                                       {
                                                            bx = newBondVectors[ndx][0];  by = newBondVectors[ndx][1];  bz = newBondVectors[ndx][2];
                                                                                 
                                                            bx_star = rotMatrix[0][0] * bx + rotMatrix[0][1] * by + rotMatrix[0][2] * bz;  
                                                            by_star = rotMatrix[1][0] * bx + rotMatrix[1][1] * by + rotMatrix[1][2] * bz;  
                                                            bz_star = rotMatrix[2][0] * bx + rotMatrix[2][1] * by + rotMatrix[2][2] * bz; 

                                                            newBondVectors[ndx][0] = bx_star;
                                                            newBondVectors[ndx][1] = by_star;
                                                            newBondVectors[ndx][2] = bz_star;
                                                       }
                                                  }
 
                                                  // Rotate bond vectors (excluding nameSelected) about nameSelected axis to obtain proper dihedral angle
                                                  nsbs = ndbs = ntbs = 0;
                                                  bvx = bvy = bvz = Dbv = 0.0;
                                                  sbNdx = dbNdx = tbNdx = -1;
                                                  bvndx1 = -1;
                                                  finalDihedralAngle = 0.0;
                                                  foundTargetAtomBond = foundAddedAtomBond = autoAdjustDihedral = false;

                                                  hybridization = getHybridization();

                                                  if (hybridization.equals("sp3"))
                                                  {
                                                       // Get bond order of bond vectors on target atom
                                                       for (ndx = 0; ndx < bondVectors.length; ndx++)
                                                       {
                                                            bvndx = ( -((int)(bondVectors[ndx][0]/100) + 1) );
  
                                                            if (bvndx == targetAtomNdx)
                                                            {
                                                                 if (bondVectors[ndx][4] == 1)
                                                                 { 
                                                                      nsbs++;
                                                                      sbNdx = ndx;
                                                                 }
                                                                 else if (bondVectors[ndx][4] == 2)
                                                                 {
                                                                      ndbs++;
                                                                      dbNdx = ndx;

                                                                      break;
                                                                 }
                                                                 else if (bondVectors[ndx][4] == 3)
                                                                 {
                                                                      ntbs++;
                                                                      tbNdx = ndx;

                                                                      break;
                                                                 }
                                                            }
                                                       }

                                                       if ( (ndbs == 0) && (ntbs == 0) )
                                                       {
                                                            for (ndx = 0; ndx < natoms; ndx++)
                                                            {
                                                                 if (atomConnectivity[targetAtomNdx][ndx] == 1)
                                                                 {
                                                                      if (nsbs == 0)
                                                                      {
                                                                           nsbs++;
          
                                                                           bvx = atomCoordinates[ndx][1] - atomCoordinates[targetAtomNdx][1];
                                                                           bvy = atomCoordinates[ndx][2] - atomCoordinates[targetAtomNdx][2];
                                                                           bvz = atomCoordinates[ndx][3] - atomCoordinates[targetAtomNdx][3];
                                                                      }
                                                                 }
                                                                 else if (atomConnectivity[targetAtomNdx][ndx] == 2)
                                                                 {
                                                                      ndbs++;
     
                                                                      bvx = atomCoordinates[ndx][1] - atomCoordinates[targetAtomNdx][1];
                                                                      bvy = atomCoordinates[ndx][2] - atomCoordinates[targetAtomNdx][2];
                                                                      bvz = atomCoordinates[ndx][3] - atomCoordinates[targetAtomNdx][3];
 
                                                                      break;
                                                                 } 
                                                                 else if (atomConnectivity[targetAtomNdx][ndx] == 3)
                                                                 {
                                                                      ntbs++;
     
                                                                      bvx = atomCoordinates[ndx][1] - atomCoordinates[targetAtomNdx][1];
                                                                      bvy = atomCoordinates[ndx][2] - atomCoordinates[targetAtomNdx][2];
                                                                      bvz = atomCoordinates[ndx][3] - atomCoordinates[targetAtomNdx][3];
     
                                                                      break;
                                                                 }
                                                            }
                                                       }     

                                                       Dbv = Math.sqrt( (bvx*bvx) * (bvy*bvy) * (bvz*bvz) );
 
                                                       if (Dbv != 0.0)
                                                       {
                                                            bvx = bvx/Dbv;  bvy = bvy/Dbv;  bvz = bvz/Dbv;
                                                       }

                                                       bvndx1 = sbNdx;
 
                                                       if (parallelVecs)         // selected bv || to bv of added atom
                                                       {
                                                            finalDihedralAngle = 120.0;
                                                       }
                                                       else
                                                       {
                                                            finalDihedralAngle = 60.0;
                                                       }
                             
                                                       if (ndbs > 0)
                                                       {
                                                            finalDihedralAngle = 90.0;
          
                                                            bvndx1 = dbNdx;
                                                       }
                                                       else if (ntbs > 0)
                                                       {
                                                            finalDihedralAngle = 90.0;

                                                            bvndx1 = tbNdx;
                                                       }

                                                       if ( (nsbs + ndbs + ntbs) > 0)
                                                       {
                                                            foundTargetAtomBond = true;
                                                       }
                                                  }
                                                  else if (hybridization.equals("sp2"))
                                                  {
                                                       if (bondOrder == 2)
                                                       {
                                                            // Get number of bond vectors on target atom
                                                            int nbdvecs = 0;
                                           
                                                            for (ndx = 0; ndx < bondVectors.length; ndx++)
                                                            {
                                                                 bvndx = ( -((int)(bondVectors[ndx][0]/100) + 1) );

                                                                 if ( (bvndx == targetAtomNdx) && (bondVectors[ndx][0] != nameSelected) )
                                                                 {
                                                                      if ( (int)(bondVectors[ndx][4]) == 1)
                                                                      {
                                                                           nsbs++;
                                                                           sbNdx = ndx;
 
                                                                           nbdvecs++;
                                                                      }
                                                                 }
                                                            }
 
                                                            if (nsbs == 0)
                                                            {
                                                                 for (ndx = 0; ndx < natoms; ndx++)
                                                                 {
                                                                      if (atomConnectivity[targetAtomNdx][ndx] == 1)
                                                                      {
                                                                           nsbs++;
      
                                                                           bvx = atomCoordinates[ndx][1] - atomCoordinates[targetAtomNdx][1];
                                                                           bvy = atomCoordinates[ndx][2] - atomCoordinates[targetAtomNdx][2];
                                                                           bvz = atomCoordinates[ndx][3] - atomCoordinates[targetAtomNdx][3];
                                                                      }
                                                                 }
                                                            }     

                                                            Dbv = Math.sqrt( (bvx*bvx) * (bvy*bvy) * (bvz*bvz) );
 
                                                            if (Dbv != 0.0)
                                                            {
                                                                 bvx = bvx/Dbv;  bvy = bvy/Dbv;  bvz = bvz/Dbv;
                                                            }

                                                            if (nsbs > 0)
                                                            {
                                                                 bvndx1 = sbNdx;

                                                                 finalDihedralAngle = 180;
               
                                                                 foundTargetAtomBond = true;
                                                            }
                                                       }
                                                       else if (bondOrder == 1)
                                                       {
                                                            //
                                                       }
                                                  }
                                                  else if (hybridization.equals("sp"))
                                                  {
                                                       // 
                                                  }

                                                  if ( (!hybridization.equals(" ")) && (foundTargetAtomBond) )
                                                  { 
                                                       // Rotate about bond axis vector
                                                       cosangle = Math.cos((Math.PI/180)*finalDihedralAngle);
                                                       sinangle = Math.sin(Math.acos(cosangle));

                                                       // Rotate bond vectors about bond axis
                                                       rotMatrix[0][0] = (tx * tx) + (cosangle * (1 - tx * tx));
                                                       rotMatrix[0][1] = ((tx * ty) * (1 - cosangle)) + (tz * sinangle);
                                                       rotMatrix[0][2] = ((tz * tx) * (1 - cosangle)) - (ty * sinangle);

                                                       rotMatrix[1][0] = ((tx * ty) * (1 - cosangle)) - (tz * sinangle);
                                                       rotMatrix[1][1] = (ty * ty) + (cosangle * (1 - ty * ty));
                                                       rotMatrix[1][2] = ((ty * tz) * (1 - cosangle)) + (tx * sinangle);
 
                                                       rotMatrix[2][0] = ((tz * tx) * (1 - cosangle)) + (ty * sinangle);
                                                       rotMatrix[2][1] = ((ty * tz) * (1 - cosangle)) - (tx * sinangle);
                                                       rotMatrix[2][2] = (tz * tz) + (cosangle * (1 - tz * tz));

                                                       // Compute rotated bond vector coordinates
                                                       for (ndx = 0; ndx < newBondVectors.length; ndx++)
                                                       {
                                                            bx = newBondVectors[ndx][0];  by = newBondVectors[ndx][1];  bz = newBondVectors[ndx][2];
                                                                               
                                                            bx_star = rotMatrix[0][0] * bx + rotMatrix[0][1] * by + rotMatrix[0][2] * bz;  
                                                            by_star = rotMatrix[1][0] * bx + rotMatrix[1][1] * by + rotMatrix[1][2] * bz;  
                                                            bz_star = rotMatrix[2][0] * bx + rotMatrix[2][1] * by + rotMatrix[2][2] * bz; 

                                                            newBondVectors[ndx][0] = bx_star;
                                                            newBondVectors[ndx][1] = by_star;
                                                            newBondVectors[ndx][2] = bz_star;
                                                       }
                                                  }         

                                                  tmpConnectivity = getAtomConnectivity();

                                                  atomConnectivity = new int[(natoms+1)][(natoms+1)];

                                                  for (j = 0; j < natoms; j++)
                                                  {
                                                       for (k = 0; k < natoms; k++)
                                                       {  
                                                            atomConnectivity[j][k] = tmpConnectivity[j][k];
                                                       }
                                                  }

                                                  tmpBondVectors = new double[nbvRows][nbvCols];

                                                  for (j = 0; j < nbvRows; j++)
                                                  {
                                                       for (k = 0; k < nbvCols; k++)
                                                       {     
                                                            tmpBondVectors[j][k] = bondVectors[j][k];
                                                       }
                                                  }

                                                  bondVectors = new double[(nbvRows + nbondvectors)][nbvCols];

                                                  for (j = 0; j < nbvRows; j++)
                                                  {
                                                       for (k = 0; k < nbvCols; k++)
                                                       {     
                                                            bondVectors[j][k] = tmpBondVectors[j][k];
                                                       }
                                                  }

                                                  bondLngth = getDefaultBondLength(targetIndx, bondedIndx, bondOrder);
                                       
                                                  atomCoordinates[natoms][0] = natoms;
                                                  atomCoordinates[natoms][1] = atomCoordinates[targetAtomNdx][1] - (tx * bondLngth);
                                                  atomCoordinates[natoms][2] = atomCoordinates[targetAtomNdx][2] - (ty * bondLngth);
                                                  atomCoordinates[natoms][3] = atomCoordinates[targetAtomNdx][3] - (tz * bondLngth);

                                                  // Get fragID of target atom and set fragID of bonded atom equal to this value
                                                  fragID = (int)atomCoordinates[targetAtomNdx][4];
 
                                                  atomCoordinates[natoms][4] = fragID;

                                                  for (ndx = 0; ndx < nbondVectors; ndx++)
                                                  {
                                                       bondVectors[(nbvRows + ndx)][0] = -((100*(natoms + 1)) + ndx);
                                                       bondVectors[(nbvRows + ndx)][1] = newBondVectors[ndx][0];
                                                       bondVectors[(nbvRows + ndx)][2] = newBondVectors[ndx][1];
                                                       bondVectors[(nbvRows + ndx)][3] = newBondVectors[ndx][2];
                                                       bondVectors[(nbvRows + ndx)][4] = newBondVectors[ndx][3];
                                                       bondVectors[(nbvRows + ndx)][5] = 1;

                                                       bvID.add((int)bondVectors[(nbvRows + ndx)][0]);
                                                  }

                                                  bondedBV = (int)bondVectors[(nbvRows + bondedBVNdx)][0];

                                                  natoms++;

                                                  bondedAtomNdx = (natoms - 1);
       
                                                  atomConnectivity[targetAtomNdx][bondedAtomNdx] = atomConnectivity[bondedAtomNdx][targetAtomNdx] = bondOrder;
                                                  nbonds++;

                                                  // Delete bond vectors involved in newly generated bond
                                                  targetBV = nameSelected;

                                                  nbvRows += nbondVectors;

                                                  tmpBondVectors = new double[nbvRows][nbvCols];

                                                  for (j = 0; j < nbvRows; j++)
                                                  {
                                                       for (k = 0; k < nbvCols; k++)
                                                       {     
                                                            tmpBondVectors[j][k] = bondVectors[j][k];
                                                       }
                                                  }

                                                  bondVectors = new double[(nbvRows - 2)][nbvCols];
 
                                                  ndx = (-1);

                                                  for (j = 0; j < nbvRows; j++)
                                                  {
                                                       bv = (int)tmpBondVectors[j][0];

                                                       if ( (bv != (int)targetBV) && (bv != (int)bondedBV) )
                                                       {
                                                            ndx++;

                                                            for (k = 0; k < nbvCols; k++)
                                                            {     
                                                                 bondVectors[ndx][k] = tmpBondVectors[j][k];
                                                            }
                                                       }
                                                  }

                                                  if ( (!hybridization.equals(" ")) && (foundTargetAtomBond) )
                                                  {
                                                       axvec.clear();  bvvec1.clear();  bvvec2.clear(); 

                                                       tx = atomCoordinates[bondedAtomNdx][1] - atomCoordinates[targetAtomNdx][1];
                                                       ty = atomCoordinates[bondedAtomNdx][2] - atomCoordinates[targetAtomNdx][2];
                                                       tz = atomCoordinates[bondedAtomNdx][3] - atomCoordinates[targetAtomNdx][3];

                                                       Dr = Math.sqrt( (tx*tx) + (ty*ty) + (tz*tz) );

                                                       tx = tx/Dr;  ty = ty/Dr;  tz = tz/Dr;

                                                       // Get dihedral angle unit vectors 
                                                       axvec.add(tx);  axvec.add(ty);  axvec.add(tz);

                                                       if (Dbv == 0.0)
                                                       {    
                                                            bvx = bondVectors[bvndx1][1];  bvy = bondVectors[bvndx1][2];  bvz = bondVectors[bvndx1][3];
                                                       }
   
                                                       bvvec1.add(bvx);  bvvec1.add(bvy);  bvvec1.add(bvz);

                                                       for (ndx = 0; ndx < bondVectors.length; ndx++)
                                                       {
                                                            bvndx = ( -((int)(bondVectors[ndx][0]/100) + 1) );
 
                                                            if (bvndx == bondedAtomNdx)
                                                            {
                                                                 bvvec2.add(bondVectors[ndx][1]);  bvvec2.add(bondVectors[ndx][2]);  bvvec2.add(bondVectors[ndx][3]);
  
                                                                 foundAddedAtomBond = true;

                                                                 break;
                                                            }
                                                       }
                                                  }

                                                  if ( (foundTargetAtomBond) && (foundAddedAtomBond) )
                                                  {
                                                       autoAdjustDihedral = true;
                                                  }
 
                                                  if (autoAdjustDihedral)
                                                  {
                                                       currentDihedralAngle = setDihedralAngle(bvvec1, bvvec2, axvec);                                             
                                                  }                                      
                                             }
                                        }

                                        // Generate a *.mol2 file 
                                        if ( (nhits > 0) && (!altKeyDown) )
                                        {
//                                             generateMol2File("New Molecule");
                                             generateMol2File(moleculeName);
                                        }

                                        displayStructure = true;

                                        selectedNames = getSelectedNames();

                                        selectMode = 1;
                                        glcanvas.repaint();
                                  }
                              }
                         }

                         displayStructure = true;

                         selectedNames = getSelectedNames();                                   

                         selectMode = 1;
                         glcanvas.repaint();
                    }
               }                        

               // Middle mouse button clicked 
               if (((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK))
               {
                    mouseButtonClicked = 2;
               }

               // Right mouse button clicked 
               if (((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)) 
               {
                    mouseButtonClicked = 3;

                    int fragmentID, selectedIndex, bndOrdr;

                    natoms = getNatoms();
                    selectedNames = getSelectedNames();

                    if (numMouseclicks == 1)
                    {
                         rotType = 0;

                         elementSelected = getElementSelected();
                         selectedNames = getSelectedNames();
                    }
                    else if (numMouseclicks == 2)
                    {
                         rotType = 0;

                         selectedNames = getSelectedNames();
                  
                         if ( (bondAngleSelected) || (dihedralAngleSelected) || (axialRotationSelected) || (bvDihedralSelected) )
                         {
                              selectedNames.clear();
                              bondAngleSelected = false;  dihedralAngleSelected = false;
                              axialRotationSelected = false; bvDihedralSelected = false;
                         }

                         if (fragmentSelected)
                         {
                              fragmentSelected = false;

                              selectedFragmentAtoms.clear();
                              selectedNames.clear();
                         }
                         else
                         {
                              if (selectedNames.size() == 1)
                              {
                                   selectedIndex = selectedNames.get(0);

                                   if ( (selectedIndex >= 0) && (selectedIndex < natoms) )
                                   {
                                        atomCoordinates = getCoordinates();

                                        fragmentID = (int)atomCoordinates[selectedIndex][4];

                                        selectedFragmentAtoms.clear();

                                        for (j = 0; j < natoms; j++)
                                        {
                                             if ( (int)atomCoordinates[j][4] == fragmentID )
                                             {
                                                  selectedNames.add(j);

                                                  selectedFragmentAtoms.add(j);
                                             }
                                        }

                                        fragmentSelected = true;

                                        bondAngleSelected = dihedralAngleSelected = false;

                                        // Compute rotation origin of selected fragment
                                        setFragmentOrigin(atomCoordinates, selectedFragmentAtoms);
                                   }     
                                   else if (selectedIndex >= 1000000) 
                                   {
                                        ndx1 = (selectedIndex/1000000) - 1;  
                                        ndx2 = (selectedIndex % 1000000);

                                        atomConnectivity = getAtomConnectivity();

                                        bndOrdr = atomConnectivity[ndx1][ndx2];
                                   
                                        if ((bndOrdr % 2) == 0)
                                        {
                                             bndOrdr = 1;
                                        }
                                        else
                                        {
                                             bndOrdr = 2;
                                        }                                 
   
                                        atomConnectivity[ndx1][ndx2] = atomConnectivity[ndx2][ndx1] = bndOrdr;
                                   }
                                   else if (selectedIndex <= (-100)) 
                                   {
                                        //
                                   }
                              }
                         }
                    }

                    selectMode = 1;
                    glcanvas.repaint();
               }

               mouseX = e.getX();
               mouseY = e.getY();
          }
     }

     class CanvasKeyListener extends KeyAdapter
     {
          public void keyPressed(KeyEvent e)
          {
               rotType = getRotType();

               if (e.getKeyCode() == KeyEvent.VK_LEFT)
               {
                    arrowKeyDown = true;
                    
                    if (rotType == 0)
                    {
                         x0 += 0.2;
                         mouseX += 0.2;
                    }
                    else if (rotType == 1)
                    {
                         selectedFragmentArray = getSelectedFragmentArray();

                         // Rotate fragments only if number of fragment atoms < total number of atoms
                         natoms = getNatoms();

                         if ( (selectedFragmentArray.size() > 0) && (selectedFragmentArray.size() < natoms) )
                         {
                              // atomCoordinates = getRotatedCoordinates(selectedFragmentArray, delAlpha);
                         }
                    }

                    glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
               {
                    arrowKeyDown = true;
                    
                    if (rotType == 0)
                    {                     
                         x0 -= 0.2;
                         mouseX -= 0.2;
                    }
                    else if (rotType == 1)
                    {
                         selectedFragmentArray = getSelectedFragmentArray();
                         rotOriginNdx = getRotOriginNdx();

                         // Rotate fragments only if number of fragment atoms < total number of atoms
                         natoms = getNatoms();
                    }

                    glcanvas.repaint();  
               }
               else if (e.getKeyCode() == KeyEvent.VK_UP)
               {
                    arrowKeyDown = true;

                    if (rotType == 0)
                    {
                         y0 += 0.2;
                         mouseY += 0.2;
                    }

                    glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_DOWN)
               {
                    arrowKeyDown = true;

                    if (rotType == 0)
                    {
                         y0 -= 0.2;
                         mouseY -= 0.2;
                    }

                    glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_E)
               {
                    if (editMode)
                    {
                         editModeOn = false;
                         editMode = false;
                         editModeLabel.setVisible(false);

                         rotBondAngle = false;

                         generateMol2File(moleculeName);

                    }
                    else if (!editMode) 
                    {
                         editModeOn = true;
                         editMode = true;
                         editModeLabel.setVisible(true);

                         if (bondAngleSelected)
                         {
                              rotBondAngle = true;
                              rotFragment = false;

                              fragmentSelected = false;
                              dihedralAngleSelected = false;

                              x_rot = true;
                         }
                         else
                         {
                              rotBondAngle = false;
                              rotFragment = true;

                              fragmentSelected = true;
                         }                         
                    }
               }
               else if (e.getKeyCode() == KeyEvent.VK_ALT)
               {
                    altKeyDown = true;
               }
               else if (e.getKeyCode() == KeyEvent.VK_CONTROL)
               {
                    cntrlKeyDown = true;
                    selectionComplete = false;

                    elemField.setText("");

               }
               else if (e.getKeyCode() == KeyEvent.VK_DELETE)
               {
                    int bvname;

                    deleteKeyDown = true;

                    natoms = getNatoms();
 
                    selectedNames = getSelectedNames();

                    nameSelected = getNameSelected();

                    translateAtoms = getTranslateAtomsState();

                    eraseMolecule = false;

                    if ( (selectedNames.size() == 0) && (natoms > 0) )
                    {
                         eraseMolecule = true;

                         // Delete all user defined molecules excluding final molecular structure
                         if (userDefnbvs.size() > 0)
                         {
                              int userDefFileNumbr;
                              String infilename;
                              File fname;

                              for (ndx = 0; ndx < userDefnbvs.size(); ndx++)
                              {
                                   userDefFileSuffix = ndx + 1;

                                   userDefFileNumbr = userDefFileSuffix + 1000;

                                   infilename = "userDefinedMolecule" + Integer.toString(userDefFileNumbr) + ".mol2";

                                   // Add path directory
                                   infilename = userDefStructureDir + infilename;

                                   fname = new File(infilename);

                                   fname.delete();
                              }

                              // Reset user defined file suffix to zero
                              userDefFileSuffix = 0;

                              // Clear user defined bond vector array
                              userDefnbvs.clear();

                              // Clear user defined rotation angle arrays
                              usrdefTheta.clear();  usrdefPhi.clear();  usrdefPsi.clear(); 
                         }
                    }
                    else if ( (selectedNames.size() > 0) && (natoms > 0) )
                    {
                         deleteKeyDown = true;

                         if (deleteKeyDown)
                         {
                              natoms = getNatoms();

                              if (selectedNames.size() == 1) 
                              {
                                   nameSelected = selectedNames.get(0);

                                   if (nameSelected >= 1000000)
                                   {
                                        int bondName, target1, target2, targetName, newbvRows, targetIndex, maxIndex;
                                        double dx, dy, dz, D;
                                        double[][]tmpBV;

                                        bondName = nameSelected;

                                        target1 = (int)(bondName/1000000) - 1;
                                        target2 = bondName % 1000000;

                                        bondOrder = atomConnectivity[target1][target2];

                                        ndx1 = (nameSelected/1000000) - 1;
                                        ndx2 = nameSelected % 1000000;

                                        selectedAtomNdx = Math.max(ndx1, ndx2);
                                        unselectedAtomNdx = Math.min(ndx1, ndx2);                                      

                                        atomConnectivity = getAtomConnectivity();
                                        atomConnectivity[ndx1][ndx2] = atomConnectivity[ndx2][ndx1] = 0;

                                        atomCoordinates = getCoordinates();

                                        atomCoordinates = getFragmentAtoms(natoms, atomCoordinates, atomConnectivity, selectedAtomNdx, unselectedAtomNdx);

                                        glcanvas.repaint();

                                        //  Note: Need to add bond vectors to each of the 2 atoms that were previously bonded
                                        newbvRows = 2;

                                        bondVectors = getBondVectors();

                                        nbvRows = bondVectors.length;

                                        tmpBV = new double[nbvRows][nbvCols];
 
                                        if (nbvRows > 0)
                                        {
                                             for (j = 0; j < nbvRows; j++)
                                             {
                                                  for (k = 0; k < nbvCols; k++)
                                                  {                                             
                                                       tmpBV[j][k] = bondVectors[j][k];
                                                  } 
                                             } 
                                        }
 
                                        atomConnectivity = getAtomConnectivity();

                                        bondVectors = new double[(nbvRows + newbvRows)][nbvCols];

                                        for (j = 0; j < nbvRows; j++)
                                        {
                                             for (k = 0; k < nbvCols; k++)
                                             {
                                                  bondVectors[j][k] = tmpBV[j][k];     
                                             }
                                        }

                                        maxIndex = -1;

                                        for (k = 0; k < nbvRows; k++)
                                        {  
                                             targetName = -((int)( (bondVectors[k][0]/100) + 1) );

                                             if (targetName == target1)
                                             {
                                                  targetIndex = (int)((-bondVectors[k][0]) % 100);
 
                                                  maxIndex = Math.max(maxIndex, targetIndex);
                                             }
                                        }

                                        dx = atomCoordinates[target2][1] - atomCoordinates[target1][1];
                                        dy = atomCoordinates[target2][2] - atomCoordinates[target1][2];
                                        dz = atomCoordinates[target2][3] - atomCoordinates[target1][3];

                                        D = Math.sqrt(dx*dx + dy*dy + dz*dz);

                                        dx = dx/D;  dy = dy/D;  dz = dz/D;

                                        bondVectors[nbvRows][0] = -( (100*(target1 + 1)) + (maxIndex + 1) );
                                        bondVectors[nbvRows][1] = dx;
                                        bondVectors[nbvRows][2] = dy;                                  
                                        bondVectors[nbvRows][3] = dz;
                                        bondVectors[nbvRows][4] = bondOrder;
                                        bondVectors[nbvRows][5] = 1;

                                        maxIndex = -1;

                                        for (k = 0; k < nbvRows; k++)
                                        {  
                                             targetName = -((int)(bondVectors[k][0]/100) + 1);

                                             if (targetName == target2)
                                             {
                                                  targetIndex = (int)((-bondVectors[k][0]) % 100);
 
                                                  maxIndex = Math.max(maxIndex, targetIndex);
                                             }
                                        }

                                        bondVectors[(nbvRows + 1)][0] = -( (100*(target2 + 1)) + (maxIndex + 1) );
                                        bondVectors[(nbvRows + 1)][1] = - dx;
                                        bondVectors[(nbvRows + 1)][2] = - dy;                                  
                                        bondVectors[(nbvRows + 1)][3] = - dz;
                                        bondVectors[(nbvRows + 1)][4] = bondOrder;
                                        bondVectors[(nbvRows + 1)][5] = 1;

                                        deleteSelectedAttributes();

                                        glcanvas.repaint();  
                                   }
                                   else if (nameSelected <= (-100))
                                   {
                                        int bv, bondedAtomName;
 
                                        bondVectors = getBondVectors();
                                        nbvRows = bondVectors.length;

                                        double[][] tmpBV = new double[nbvRows][nbvCols];

                                        for (j = 0; j < nbvRows; j++)
                                        {
                                             for (k = 0; k < nbvCols; k++)
                                             {
                                                  tmpBV[j][k] = bondVectors[j][k];
                                             }
                                        }

                                        bondVectors = new double[(nbvRows-1)][nbvCols];
 
                                        indx = -1;

                                        for (j = 0; j < nbvRows; j++)
                                        {

                                             if (tmpBV[j][0] != nameSelected)
                                             {
                                                  indx++;

                                                  for (k = 0; k < nbvCols; k++)
                                                  {                                             
                                                       bondVectors[indx][k] = tmpBV[j][k];
                                                  }
                                             }
                                        }

                                        glcanvas.repaint();
                                   }
                                   else if ( (nameSelected >= 0) && (nameSelected < natoms) )
                                   {
                                        // Get all atoms to which the delected atom was bonded
                                        int ndx1, ndx2, indx1, indx2;
                                        int deletedAtomNdx, bondedName, targetName, targetIndex, selectedBVNdx, maxIndex;
                                        int nrows, nbondedAtoms, bondedAtomIndx, bvName, newbvRows, ndeletedBVs, suffix;
                                        int[][] tmpConnectivity;
                                        double bvx, bvy, bvz, dx, dy, dz, D;
                                        double[][] tmpCoords;  
                                        double[][] tmpBV; double[][] newTmpBV;
                                        double[][] newBondVectors;
                                        ArrayList<Integer> bondedAtoms = new ArrayList<Integer>();
                                        ArrayList<Integer> bondorder = new ArrayList<Integer>();
                                        ArrayList<Integer> bvsuffix = new ArrayList<Integer>();
                                        ArrayList<Integer> connCols = new ArrayList<Integer>();
                                        ArrayList<ArrayList<Integer>> connRows = new ArrayList<ArrayList<Integer>>();

                                        deletedAtomNdx = nameSelected;

                                        tmpCoords = getCoordinates();
                                        tmpConnectivity = getAtomConnectivity();
                                        tmpBV = getBondVectors();
           
                                        elemID.remove(deletedAtomNdx);
                                        atomNames.clear();
                                        setAtomNames(elemID);
                                        atomNames = getAtomNames();

                                        AN.remove(deletedAtomNdx);
           
                                        setElementRGB(AN, (natoms-1));
                                        elementRGB = new float[(natoms-1)][ncolors];
                                        elementRGB = getElementColors();

                                        atomRadii.remove(deletedAtomNdx);
 
                                        // Get atoms bonded to deleted atom
                                        for (ndx = 0; ndx < natoms; ndx++)
                                        {
                                             if (tmpConnectivity[deletedAtomNdx][ndx] > 0)
                                             {  
                                                  bondedAtoms.add(ndx);
                                                  bondorder.add(tmpConnectivity[deletedAtomNdx][ndx]);
                                             }    
                                        }

                                        // Tag for deletion the bond vectors on deleted atom
                                        nbvRows = tmpBV.length;

                                        ndeletedBVs = 0;
                                        for (ndx = 0; ndx < nbvRows; ndx++)
                                        {
                                             bvName = (int)tmpBV[ndx][0];
                                             selectedBVNdx = - ((int)(bvName/100) + 1);

                                             if (selectedBVNdx == deletedAtomNdx)
                                             {
                                                  tmpBV[ndx][0] = (-1);
                                                  ndeletedBVs++;
                                             }
                                        }

                                        nbondedAtoms = bondedAtoms.size();
                                        newBondVectors = new double[nbondedAtoms][nnewbvCols];

                                        for (ndx = 0; ndx < nbondedAtoms; ndx++)
                                        {
                                             bondedAtomIndx = bondedAtoms.get(ndx);

                                             bvx = tmpCoords[deletedAtomNdx][1] - tmpCoords[bondedAtomIndx][1];
                                             bvy = tmpCoords[deletedAtomNdx][2] - tmpCoords[bondedAtomIndx][2];
                                             bvz = tmpCoords[deletedAtomNdx][3] - tmpCoords[bondedAtomIndx][3];

                                             D = Math.sqrt( (bvx*bvx) + (bvy*bvy) + (bvz*bvz) );

                                             bvx = bvx/D;  bvy = bvy/D;  bvz = bvz/D;

                                             newBondVectors[ndx][1] = bvx;  newBondVectors[ndx][2] = bvy;  newBondVectors[ndx][3] = bvz;
                                             newBondVectors[ndx][4] = bondorder.get(ndx);
                                             newBondVectors[ndx][5] = 1;

                                             bvsuffix.clear();

                                             for (ndx1 = 0; ndx1 < nbvRows; ndx1++)
                                             {
                                                  bvName = (int)tmpBV[ndx1][0];
                                                  selectedBVNdx = - ((int)(bvName/100) + 1);

                                                  if (selectedBVNdx == bondedAtomIndx)
                                                  {
                                                       suffix = -(bvName % 100);
                                                       bvsuffix.add(suffix);
                                                  }
                                             }

                                             for (ndx2 = 1; ndx2 < 7; ndx2++)
                                             {
                                                  if (!bvsuffix.contains(ndx2))
                                                  {
                                                       newBondVectors[ndx][0] = -((100*(bondedAtomIndx+1)) + ndx2); 

                                                       break;
                                                  }
                                             }
                                        }

                                        // Construct new bond vector matrix
                                        nbvRows -= ndeletedBVs;

                                        newTmpBV = new double[nbvRows][nbvCols];

                                        indx = -1;

                                        for (j = 0; j < tmpBV.length; j++)
                                        {
                                             if (tmpBV[j][0] < (-1))
                                             {
                                                  indx++;

                                                  for (k = 0; k < nbvCols; k++)
                                                  {
                                                       newTmpBV[indx][k] = tmpBV[j][k];
                                                  }
                                             }
                                        }
 
                                        // Add new bond vectors to bond vector matrix
                                        nbvRows += nbondedAtoms;

                                        bondVectors = new double[nbvRows][nbvCols];

                                        // Add current bond vectors
                                        nrows = newTmpBV.length;

                                        for (j = 0; j < nrows; j++) 
                                        {
                                             for (k = 0; k < nbvCols; k++)
                                             {
                                                  bondVectors[j][k] = newTmpBV[j][k];
                                             }
                                        }
           
                                        // Add new bond vectors
                                        for (j = 0; j < nbondedAtoms; j++) 
                                        {
                                             for (k = 0; k < nbvCols; k++)
                                             {
                                                  bondVectors[(j + nrows)][k] = newBondVectors[j][k];
                                             }
                                        }           

                                        // Modify bond vectors names to account for deleted atom
                                        for (ndx = 0; ndx < bondVectors.length; ndx++)
                                        {
                                             bvName = (int)bondVectors[ndx][0];
                                             selectedBVNdx = - ((int)(bvName/100) + 1);
                                             suffix = -(bvName % 100);

                                             if (selectedBVNdx > deletedAtomNdx)
                                             {
                                                  indx = (selectedBVNdx - 1);    
                        
                                                  bondVectors[ndx][0] = -( (100*(indx+1)) + suffix);
                                             }
                                        }

                                        int[][] tmpConn2 = new int[(natoms-1)][natoms];

                                        ndx = -1;

                                        for (j = 0; j < natoms; j++)
                                        {
                                             if (j != deletedAtomNdx)
                                             {   
                                                  ndx++;

                                                  for (k = 0; k < natoms; k++)
                                                  {
                                                       tmpConn2[ndx][k] = tmpConnectivity[j][k];
                                                  }
                                             }
                                        }

                                        atomConnectivity = new int[(natoms-1)][(natoms-1)];

                                        for (j = 0; j < (natoms-1); j++)
                                        {
                                             ndx = -1;
 
                                             for (k = 0; k < natoms; k++)
                                             {
                                                  if (k != deletedAtomNdx)
                                                  {   
                                                       ndx++;

                                                       atomConnectivity[j][ndx] = tmpConn2[j][k];
                                                  }
                                             }
                                        }     
        
                                        // Remove deleted atom from atom coordinates matrix
                                        atomCoordinates = new double[(natoms-1)][ncoordCols];
 
                                        indx = -1;

                                        for (j = 0; j < natoms; j++)
                                        {
                                             if (j != deletedAtomNdx)
                                        {
                                             indx++;
  
                                             for (k = 0; k < ncoordCols; k++)
                                             {
                                                  atomCoordinates[indx][k] = tmpCoords[j][k];
                                             }
                                        }
                                   }
 
                                   natoms--;
 
                                   selectedNames.clear();

                                   glcanvas.repaint();

                                   }
                              }   
                         }

                         generateMol2File(moleculeName);

                         glcanvas.repaint();
                    }
               }
               else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
               {
                    escapeKeyDown = true;

                    addSolventH2O = false;
                    solvateMoleculeLabel.setVisible(false);

                    if (editMode)
                    {
                         editModeOn = false;
                         editMode = false;
                         editModeLabel.setVisible(false);

                         rotBondAngle = false;

                         generateMol2File(moleculeName);
                    }
                    else
                    {
                         drawMode = false;
                         sketchBond = false;
                         drawFcnalGrp = false;
                         drawFcnalGrpPopupSubmenu.setVisible(false);
                         drawFragmentPopupSubmenu.setVisible(false);

                         elementList.setVisible(false);
                         addElement = false;
                    }

                    glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_SHIFT)
               {
                    shiftKeyDown = true;

                    selectedNames = getSelectedNames();

                    setSelectedFragments(selectedNames);

                    if (selectedNames.size() > 0)
                    {
                         rotationAxis = getRotationAxis();

                         glcanvas.repaint();
                    }
               }
               else if (e.getKeyCode() == KeyEvent.VK_SPACE)
               {
                    spaceKeyDown = true;

                    elemID = getElemID();

                    addHydrogens();

                    // Generate atom types for this molecule
//                    generateAtomTypes(userDefStructureDir + "newMolecule.mol2");
//                    atomTypes.clear();
//                    atomTypes = getAtomTypes("cgenff_out.txt");

                    glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_T)
               {
                     tKeyDown = true;
               }
               else if (e.getKeyCode() == KeyEvent.VK_X)
               {
                     xKeyDown = true;
                     xyzKeys.set(0, 1);
                     xyzKeyDown = true;

keyID = "x";
               }
               else if (e.getKeyCode() == KeyEvent.VK_Y)
               {
                     yKeyDown = true;
                     xyzKeys.set(1, 1); 
                     xyzKeyDown = true;                    

keyID = "y";
               }
               else if (e.getKeyCode() == KeyEvent.VK_Z)
               {
                     zKeyDown = true;
                     xyzKeys.set(2, 1);
                     xyzKeyDown = true;

keyID = "z";
               }

if (xyzKeyDown)
{
//     xyzKeysDepressed = true;
}

               // Hide all popup menus that are visible
               hidePopupMenus();

               // Hide all popup submenus that are visible
               hidePopupSubmenus();          
          }

          public void keyReleased(KeyEvent e)
          {
//               if ( (e.getKeyCode() == KeyEvent.VK_X) || (e.getKeyCode() == KeyEvent.VK_Y) || (e.getKeyCode() == KeyEvent.VK_Z) )
               if (e.getKeyCode() == KeyEvent.VK_X)
               {
                    xKeyDown = false;
                    xyzKeyDown = false;     //reorientRotationAxes();
                    xyzKeys.set(10, 1);
               }
               if (e.getKeyCode() == KeyEvent.VK_Y)
               {
                    yKeyDown = false;
                    xyzKeyDown = false;     //reorientRotationAxes();
                    xyzKeys.set(10, 1);
               }
               if (e.getKeyCode() == KeyEvent.VK_Z)
               {
                    zKeyDown = false;
                    xyzKeyDown = false;     //reorientRotationAxes();
                    xyzKeys.set(10, 1);
               }
               if (e.getKeyCode() == KeyEvent.VK_T)
               {
                    tKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_LEFT)
               {
                    arrowKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_RIGHT)
               {
                    arrowKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_UP)
               {
                    arrowKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_DOWN)
               {
                    arrowKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_ALT)
               {
                    altKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_CONTROL)
               {
                    int name1, name2, selectedNdx, unselectedNdx, maxFragmentID, minFragmentID;;
                    int textFlag, natm, nbnd, nbv, ndx1, ndx2, ndx3, ndx4, cntrAtmNdx, cntrNdx1, cntrNdx2, endNdx1, endNdx2;
                    int atomndx1, atomndx2, atomndx3, atomndx4, trgndx, axisAtom1, axisAtom2, bvndx1, bvndx2;

                    ArrayList<Integer> atmSelectn = new ArrayList<Integer>();
                    ArrayList<Integer> bndSelectn = new ArrayList<Integer>();
                    ArrayList<Integer> bvSelectn = new ArrayList<Integer>();

                    cntrlKeyDown = false;
                    selectionComplete = true;
                    rotType = 0;

                    natoms = getNatoms();

                    selectedNames = getSelectedNames();

                    if (selectedNames.size() > 0)
                    {
                         if (selectedNames.size() <= 5)
                         {
                              strBondAngle = strDihedralAngle = "";

                              getSelectedComponent(natoms, selectedNames);
                         }
                    }

                    if (selectedNames.size() == 2)
                    {
                         bondAngleSelected = false;
                         dihedralAngleSelected = false;
                         bvDihedralSelected = false;

                         name1 = selectedNames.get(0);
                         name2 = selectedNames.get(1);

                         selectedNdx = unselectedNdx = -1;

                         if ( (name1 <= (-100)) && (name2 <= (-100)) )
                         {
                              int atom1Ndx, atom2Ndx, bv1Ndx, bv2Ndx;   
                              double bvx1, bvy1, bvz1, bvx2, bvy2, bvz2, Rbvx, Rbvy, Rbvz, R1, R2, bvdot12;
                              double bondX, bondY, bondZ, R12, bonddot12;
                              double[][] tmpBV; 

                              bv1Ndx = bv2Ndx = -1;
                              bonddot12 = 0.0;

                              axialRotationSelected = false;

                              addBond = getAddBondState();

                              atom1Ndx = ( -((int)(name1/100) + 1) );
                              atom2Ndx = ( -((int)(name2/100) + 1) );

                              bondVectors = getBondVectors();
                              nbvRows = bondVectors.length;

                              if (addBond)
                              {
                                   for (j = 0; j < nbvRows; j++)
                                   {
                                        if (bondVectors[j][0] == name1)
                                        {
                                             bv1Ndx = j;
                                        }
                                        else if (bondVectors[j][0] == name2)
                                        {
                                             bv2Ndx = j;
                                        }
                                   }                                   

                                   // Determine alignment of bond vectors
                                   bvx1 = bondVectors[bv1Ndx][1];  bvy1 = bondVectors[bv1Ndx][2]; bvz1 = bondVectors[bv1Ndx][3];
                                   bvx2 = bondVectors[bv2Ndx][1];  bvy2 = bondVectors[bv2Ndx][2]; bvz2 = bondVectors[bv2Ndx][3];

                                   Rbvx = bvy1*bvz2 - bvy2*bvz1;  Rbvy = bvz1*bvx2 - bvz2*bvx1;  Rbvz = bvx1*bvy2 - bvx2*bvy1;
                                   R1 = Math.sqrt(bvx1*bvx1 + bvy1*bvy1 + bvz1*bvz1);
                                   R2 = Math.sqrt(bvx2*bvx2 + bvy2*bvy2 + bvz2*bvz2);

                                   bvdot12 = bvx1*bvx2 + bvy1*bvy2 + bvz1*bvz2;

                                   // Antiparallel bond vectors within the same fragment cannot be misaligned by more
                                   //   than 'bvConnTol' degrees or bond will not be drawn 
                                   if ( (((Math.sqrt(Rbvx*Rbvx + Rbvy*Rbvy + Rbvz*Rbvz))/(R1*R2)) <= Math.sin(bvConnTolAngle*Math.PI/180)) && (bvdot12 < 0) )
                                   {
                                        addBond = true;
                                   }     
                                   else
                                   {
                                        if (atomCoordinates[atom1Ndx][4] == atomCoordinates[atom2Ndx][4])
                                        { 
                                             addBond = false;
                                        }
                                   }

                                   if (addBond)
                                   {
                                        // Determine direction of bond that will be drawn
                                        bondX = atomCoordinates[atom2Ndx][1] - atomCoordinates[atom1Ndx][1];
                                        bondY = atomCoordinates[atom2Ndx][2] - atomCoordinates[atom1Ndx][2];
                                        bondZ = atomCoordinates[atom2Ndx][3] - atomCoordinates[atom1Ndx][3];

                                        R12 = Math.sqrt( (bondX*bondX) + (bondY*bondY) + (bondZ*bondZ) );

                                        bondX = bondX/R12;  bondY = bondY/R12;  bondZ = bondZ/R12;

                                        bonddot12 = (bondX*bvx1) + (bondY*bvy1) + (bondZ*bvz1);

                                        // Direction of bond between atoms and bond vector cannot be misaligned by more 
                                        //   than 'bvConnTol' degrees or bond will not be drawn 
                                        if (bonddot12 >= Math.cos(bvConnTolAngle*Math.PI/180))
                                        {
                                             addBond = false;
                                        }
                                   }
                              }

                              if (addBond)
                              {
                                   tmpBV = new double[nbvRows][nbvCols];
                                   for (j = 0; j < nbvRows; j++)
                                   {
                                        for (k = 0; k < nbvCols; k++)
                                        {
                                             tmpBV[j][k] = bondVectors[j][k];
                                        }
                                   }

                                   if (bondVectors[bv1Ndx][4] == bondVectors[bv2Ndx][4])
                                   {
                                        if (joinFragments)
                                        {
                                             maxFragmentID = (int)Math.max(atomCoordinates[atom1Ndx][4], atomCoordinates[atom2Ndx][4]);
                                             minFragmentID = (int)Math.min(atomCoordinates[atom1Ndx][4], atomCoordinates[atom2Ndx][4]);

                                             for (ndx = 0; ndx < natoms; ndx++)
                                             {
                                                  if (atomCoordinates[ndx][4] == maxFragmentID)
                                                  {
                                                       atomCoordinates[ndx][4] = minFragmentID;
                                                  }

                                                  if (atomCoordinates[ndx][4] > maxFragmentID)
                                                  {
                                                       atomCoordinates[ndx][4]--;
                                                  }
                                             }
                                        }
  
                                        if (atomCoordinates[atom1Ndx][4] == atomCoordinates[atom2Ndx][4])
                                        {
                                             atomConnectivity[atom1Ndx][atom2Ndx] = atomConnectivity[atom2Ndx][atom1Ndx] = (int)bondVectors[bv1Ndx][4];

                                             bondVectors = new double[(nbvRows - 2)][nbvCols];

                                             ndx = -1;

                                             for (j = 0; j < nbvRows; j++)
                                             {
                                                  if ( ((int)tmpBV[j][0] == name1) || ((int)tmpBV[j][0] == name2) ) 
                                                  {
                                                       // Ignore
                                                  }
                                                  else
                                                  {
                                                       ndx++;
 
                                                       for (k = 0; k < nbvCols; k++)
                                                       {
                                                            bondVectors[ndx][k] = tmpBV[j][k];
                                                       }
                                                  }
                                             }
 
                                             // Decrease the number of substructures by 1
                                             nsubstructures--;

                                             joinFragments = false;
                                             editModeOn = editMode = false;
                                             editModeLabel.setVisible(false);
                                        }
                                   }

                                   glcanvas.repaint();
                              }
                         }
                         else if ( (name1 >= 0) && (name1 < natoms) && (name2 >= 0) && (name2 < natoms) )
                         {
                              addBond = getAddBondState();

                              if (addBond)
                              {
                                   atomConnectivity[name1][name2] = atomConnectivity[name2][name1] = 1;
                              }

                              addBond = false;

                              glcanvas.repaint();
                         }            
                         else if ( (name1 >= 1000000) && (name2 >= 0) && (name2 < natoms) )
                         {
                              selectedNdx = name2;

                              rotType = 2;

                              ndx1 = (int)(name1/1000000) - 1; 
                              ndx2 = name1 % 1000000;

                              if (ndx1 == selectedNdx)
                              {
                                   unselectedNdx = ndx2;
                              }
                              else if (ndx2 == selectedNdx)
                              {
                                   unselectedNdx = ndx1;
                              }
                              else if ( (ndx1 != selectedNdx) && (ndx2 != selectedNdx) )
                              {
                                   rotType = 0;
                              }

                              glcanvas.repaint();
                         }
                         else if ( (name2 >= 1000000) && (name1 >= 0) && (name1 < natoms) )
                         {
                              selectedNdx = name1;

                              rotType = 2;

                              ndx1 = (int)(name2/1000000) - 1; 
                              ndx2 = name2 % 1000000;

                              if (ndx1 == selectedNdx)
                              {
                                   unselectedNdx = ndx2;
                              }
                              else if (ndx2 == selectedNdx)
                              {
                                   unselectedNdx = ndx1;
                              }
                              else if ( (ndx1 != selectedNdx) && (ndx2 != selectedNdx) )
                              {
                                   rotType = 0;
                              }

                              glcanvas.repaint();
                         }

                         if (rotType == 2)
                         {
                              double[][] fragmentCoordinates = new double[natoms][ncoordCols];

                              fragmentCoordinates = getCoordinates();

                              atomConnectivity = getAtomConnectivity();

                              bondConnectivity = new int[natoms][natoms];

                              for (j = 0; j < natoms; j++)
                              {
                                   for (k = 0; k < natoms; k++) 
                                   {       
                                        bondConnectivity[j][k] = atomConnectivity[j][k];
                                   }
                              }

                              bondConnectivity[selectedNdx][unselectedNdx] = bondConnectivity[unselectedNdx][selectedNdx] = 0;

                              axialRotationSelected = true;

                              axisAtom2 = selectedNdx;  axisAtom1 = unselectedNdx;

                              setAxialFragmentAtoms(natoms, atomCoordinates, atomConnectivity, axisAtom1, axisAtom2);

                              if (axialFragmentAtoms.contains(axisAtom1))
                              {
                                   axialRotationSelected = false;
                              }

                              if (axialRotationSelected)
                              {
                                   attributeModified = 4;

                                   if (editMode)
                                   {
                                        rotateFragments(attributeModified, delAxialAngle);
                                   }
                              }
                         }
                    }
                    else if (selectedNames.size() == 3)
                    {
                         int axisAtom, targetAtom, nonAxisAtom1, nonAxisAtom2;

                         double axvecx, axvecy, axvecz, Daxvec, bvvecx, bvvecy, bvvecz, Dbvvec;
                         boolean foundCntrAtom, foundDihedral; 

                         ArrayList<Double> axvec = new ArrayList<Double>();
                         ArrayList<Double> bvvec1 = new ArrayList<Double>();
                         ArrayList<Double> bvvec2 = new ArrayList<Double>();

                         natm = nbnd = nbv = 0;
 
                         axialRotationSelected = false;

                         for (j = 0; j < selectedNames.size(); j++)
                         {
                              if ( (selectedNames.get(j) >= 0) && (selectedNames.get(j) < natoms) )
                              {
                                   atmSelectn.add(selectedNames.get(j));

                                   natm++;
                              }
                              else if (selectedNames.get(j) > 1000000)
                              {
                                   bndSelectn.add(selectedNames.get(j));

                                   nbnd++;
                              }
                              else if (selectedNames.get(j) <= (-100))
                              {
                                   bvSelectn.add(selectedNames.get(j));

                                   nbv++;
                              }
                         }

                         if (natm == 3)
                         {
                              textFlag = 3;

                              deleteSelectedAttributes();                  
                              writeSelectedAttributes(selectedNames, textFlag);
                         }
                         else if ( (natm == 1) && (nbnd == 2) )
                         {
                              cntrAtmNdx = atmSelectn.get(0);
                              endNdx1 = endNdx2 = -1;

                              ndx1 = (int)(bndSelectn.get(0)/1000000) - 1; 
                              ndx2 = bndSelectn.get(0) % 1000000;

                              if (ndx1 == cntrAtmNdx)
                              {
                                   endNdx1 = ndx2;
                                   cntrNdx1 = ndx1;
                              }
                              else if (ndx2 == cntrAtmNdx)
                              {
                                   endNdx1 = ndx1;
                                   cntrNdx1 = ndx2;
                              }                            
                              else
                              {
                                   cntrNdx1 = -1;
                              }

                              ndx1 = (int)(bndSelectn.get(1)/1000000) - 1; 
                              ndx2 = bndSelectn.get(1) % 1000000;

                              if (ndx1 == cntrAtmNdx)
                              {
                                   endNdx2 = ndx2;
                                   cntrNdx2 = ndx1;
                              }
                              else if (ndx2 == cntrAtmNdx)
                              {
                                   endNdx2 = ndx1;
                                   cntrNdx2 = ndx2;
                              }                            
                              else
                              {
                                   cntrNdx2 = -1;
                              }

                              if ( (cntrNdx1 == cntrAtmNdx) && (cntrNdx2 == cntrAtmNdx) )
                              {
                                   selectedComponents.clear();

                                   selectedComponents.add(endNdx1);
                                   selectedComponents.add(cntrAtmNdx);
                                   selectedComponents.add(endNdx2);

                                   textFlag = 3;

                                   deleteSelectedAttributes();                  
                                   writeSelectedAttributes(selectedComponents, textFlag);                                   

                                   bondAngleSelected = true;
                                   dihedralAngleSelected = false; 

                                   setBondAngleFragments(natoms, atomCoordinates, atomConnectivity, endNdx1, endNdx2, cntrAtmNdx);
                              }
                         }
                         else if ( (natm == 0) && (nbnd == 3) )
                         {
                              int ndx11, ndx12, ndx21, ndx22, ndx31, ndx32;                             

                              ArrayList<Integer> dihedralBondIndices = new ArrayList<Integer>();
                              ArrayList<Integer> bondAxisIndices = new ArrayList<Integer>();
                              ArrayList<Integer> nonAxisIndices = new ArrayList<Integer>();

minDHCentralBondIndices.clear();  maxDHCentralBondIndices.clear();

                              ndx11 = (int)(selectedNames.get(0)/1000000) - 1;
                              dihedralBondIndices.add(ndx11);
                              ndx12 = selectedNames.get(0) % 1000000;
                              dihedralBondIndices.add(ndx12);

                              ndx21 = (int)(selectedNames.get(1)/1000000) - 1;
                              dihedralBondIndices.add(ndx21);
                              ndx22 = selectedNames.get(1) % 1000000;
                              dihedralBondIndices.add(ndx22);

                              ndx31 = (int)(selectedNames.get(2)/1000000) - 1;
                              dihedralBondIndices.add(ndx31);
                              ndx32 = selectedNames.get(2) % 1000000;
                              dihedralBondIndices.add(ndx32);
                           
                              for (j = 1; j < dihedralBondIndices.size(); j++)
                              {
                                   for (k = 0; k < j; k++)
                                   {
                                        index = dihedralBondIndices.get(j);

                                        if (index == dihedralBondIndices.get(k))
                                        {
                                             bondAxisIndices.add(index);
                                        }
                                   }
                              }

                              dihedralAngleSelected = false;

                              if (bondAxisIndices.size() == 2)
                              {
                                   dihedralAngleSelected = true;

idCentralDhBond = true;

                                   bondAngleSelected = false;
                              }

                              if (dihedralAngleSelected)
                              {
                                   cntrNdx1 = bondAxisIndices.get(0);  cntrNdx2 = bondAxisIndices.get(1);


minDHCentralBondIndices.add(Math.min(cntrNdx1, cntrNdx2));  maxDHCentralBondIndices.add(Math.max(cntrNdx1, cntrNdx2));
  

                                   for (j = 0; j < dihedralBondIndices.size(); j++)
                                   {
                                        index = dihedralBondIndices.get(j);
 
                                        if ( (!bondAxisIndices.contains(index)) && (!nonAxisIndices.contains(index)) )
                                        {
                                             nonAxisIndices.add(index);
                                        }
                                   } 
  
                                   axisAtom = bondAxisIndices.get(0);
                                   targetAtom = nonAxisIndices.get(0);

                                   if (atomConnectivity[axisAtom][targetAtom] > 0)
                                   {
                                        axisAtom1 = axisAtom;  axisAtom2 = bondAxisIndices.get(1);
                                        nonAxisAtom1 = targetAtom;  nonAxisAtom2 = nonAxisIndices.get(1);
                                   }
                                   else
                                   {
                                        axisAtom2 = axisAtom;  axisAtom1 = bondAxisIndices.get(1);
                                        nonAxisAtom2 = targetAtom;  nonAxisAtom1 = nonAxisIndices.get(1);
                                   }

                                   selectedComponents.clear();

                                   selectedComponents.add(nonAxisAtom1);  selectedComponents.add(axisAtom1);
                                   selectedComponents.add(axisAtom2);  selectedComponents.add(nonAxisAtom2);

                                   textFlag = 4;
                              
                                   deleteSelectedAttributes(); 
                                   writeSelectedAttributes(selectedComponents, textFlag);   

                                   setDihedralAngleFragments(natoms, atomCoordinates, atomConnectivity, axisAtom1, axisAtom2);
                              }

                              glcanvas.repaint();
                         }
                         else if ( (nbnd == 2) && (nbv == 1) )
                         {
                              axisAtom1 = axisAtom2 = nonAxisAtom1 = (-1);
                              axvecx = axvecy = axvecz = 0.0;

                              axisAtom2 = (-((int)(bvSelectn.get(0)/100) + 1) );

                              atomndx1 = (int)(bndSelectn.get(0)/1000000) - 1;
                              atomndx2 = bndSelectn.get(0) % 1000000;
                              atomndx3 = (int)(bndSelectn.get(1)/1000000) - 1;
                              atomndx4 = bndSelectn.get(1) % 1000000;

                              foundCntrAtom = false;
                              foundDihedral = false;

                              if (atomndx3 == axisAtom2)
                              {
                                   axisAtom1 = atomndx4;

                                   foundCntrAtom = true;
                              }
                              else if (atomndx4 == axisAtom2)
                              {
                                   axisAtom1 = atomndx3;

                                   foundCntrAtom = true;
                              }

                              if (foundCntrAtom)
                              {
                                   if (atomndx1 == axisAtom1)
                                   {
                                       nonAxisAtom1 = atomndx2;

                                       foundDihedral = true;
                                   }
                                   else if (atomndx2 == axisAtom1)
                                   {
                                       nonAxisAtom1 = atomndx1;

                                       foundDihedral = true;
                                   }
                              }

                              if (foundDihedral)
                              {
                                   axvecx = atomCoordinates[axisAtom2][1] - atomCoordinates[axisAtom1][1];
                                   axvecy = atomCoordinates[axisAtom2][2] - atomCoordinates[axisAtom1][2];
                                   axvecz = atomCoordinates[axisAtom2][3] - atomCoordinates[axisAtom1][3];

                                   Daxvec = Math.sqrt( (axvecx*axvecx) + (axvecy*axvecy) + (axvecz*axvecz) );

                                   axvec.add(axvecx/Daxvec);  axvec.add(axvecy/Daxvec);  axvec.add(axvecz/Daxvec);

                                   for (j = 0; j < bondVectors.length; j++)
                                   {
                                        if (bondVectors[j][0] == bvSelectn.get(0))
                                        {
                                             bvvec2.add(bondVectors[j][1]);  bvvec2.add(bondVectors[j][2]);  bvvec2.add(bondVectors[j][3]);       
                                        }
                                   }

                                   bvvecx = atomCoordinates[nonAxisAtom1][1] - atomCoordinates[axisAtom1][1];
                                   bvvecy = atomCoordinates[nonAxisAtom1][2] - atomCoordinates[axisAtom1][2];
                                   bvvecz = atomCoordinates[nonAxisAtom1][3] - atomCoordinates[axisAtom1][3];

                                   Dbvvec = Math.sqrt( (bvvecx*bvvecx) + (bvvecy*bvvecy) + (bvvecz*bvvecz) );

                                   bvvec1.add(bvvecx/Dbvvec);  bvvec1.add(bvvecy/Dbvvec);  bvvec1.add(bvvecz/Dbvvec);

                                   dihedralAngleSelected = true;
                                   bvDihedralSelected  = true;

                                   selectedComponents.clear();

                                   selectedComponents.add(nonAxisAtom1);  selectedComponents.add(axisAtom1);
                                   selectedComponents.add(axisAtom2);  selectedComponents.add(bvSelectn.get(0));

                                   textFlag = 4;

                                   dihedralAngle = setDihedralAngle(bvvec1, bvvec2, axvec); 

                                   strDihedralAngle = Double.toString(dihedralAngle);
                                   int pointNdx = strDihedralAngle.indexOf(".");

                                   strlngth = Math.min( (strDihedralAngle.length() - pointNdx), pointNdx+3); 
                                   strDihedralAngle = strDihedralAngle.substring(0, strlngth);

                                   dihedralAngleLabel.setVisible(true);
                                   dihedralAngleField.setVisible(true);
                                   dihedralAngleField.setText(strDihedralAngle);                                   

                                   setDihedralAngleFragments(natoms, atomCoordinates, atomConnectivity, axisAtom1, axisAtom2);
                              }

                              glcanvas.repaint();
                         }
                         else if ( (nbnd == 1) && (nbv == 2) )
                         {
                              axisAtom1 = -((int)(bvSelectn.get(0)/100) + 1);
                              axisAtom2 = -((int)(bvSelectn.get(1)/100) + 1);

                              atomndx1 = (int)(bndSelectn.get(0)/1000000) - 1;
                              atomndx2 = bndSelectn.get(0) % 1000000;

                              foundDihedral = false; 
                              axvecx = axvecy = axvecz = 0.0;

                              if ( (atomndx1 == axisAtom1) && (atomndx2 == axisAtom2) )
                              {
                                   foundDihedral = true;
                              }
                              else if ( (atomndx2 == axisAtom1) && (atomndx1 == axisAtom2) )
                              {
                                   foundDihedral = true;
                              }
 
                              if (foundDihedral)
                              {
                                   axvecx = atomCoordinates[atomndx2][1] - atomCoordinates[atomndx1][1];
                                   axvecy = atomCoordinates[atomndx2][2] - atomCoordinates[atomndx1][2];
                                   axvecz = atomCoordinates[atomndx2][3] - atomCoordinates[atomndx1][3];

                                   Daxvec = Math.sqrt( (axvecx*axvecx) + (axvecy*axvecy) + (axvecz*axvecz) );

                                   axvec.add(axvecx/Daxvec);  axvec.add(axvecy/Daxvec);  axvec.add(axvecz/Daxvec);

                                   for (j = 0; j < bondVectors.length; j++)
                                   {
                                        if (bondVectors[j][0] == bvSelectn.get(0))
                                        {
                                             bvvec1.add(bondVectors[j][1]);  bvvec1.add(bondVectors[j][2]);  bvvec1.add(bondVectors[j][3]);       
                                        }
                                        else if (bondVectors[j][0] == bvSelectn.get(1))
                                        {
                                             bvvec2.add(bondVectors[j][1]);  bvvec2.add(bondVectors[j][2]);  bvvec2.add(bondVectors[j][3]);       
                                        }
                                   }

                                   dihedralAngleSelected = true;
                                   bvDihedralSelected  = true;

                                   selectedComponents.clear();

                                   selectedComponents.add(bvSelectn.get(0));  selectedComponents.add(axisAtom1);
                                   selectedComponents.add(axisAtom2);  selectedComponents.add(bvSelectn.get(1));

                                   textFlag = 4;

                                   dihedralAngle = setDihedralAngle(bvvec1, bvvec2, axvec); 

                                   strDihedralAngle = Double.toString(dihedralAngle);
                                   int pointNdx = strDihedralAngle.indexOf(".");

                                   strlngth = Math.min( (strDihedralAngle.length() - pointNdx), pointNdx+3); 
                                   strDihedralAngle = strDihedralAngle.substring(0, strlngth);

                                   dihedralAngleLabel.setVisible(true);
                                   dihedralAngleField.setVisible(true);
                                   dihedralAngleField.setText(strDihedralAngle);

                                   setDihedralAngleFragments(natoms, atomCoordinates, atomConnectivity, axisAtom1, axisAtom2);
                              }
                         }

                         glcanvas.repaint();
                    }
               }
               if (e.getKeyCode() == KeyEvent.VK_DELETE)
               {
                    deleteKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
               {
                    escapeKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_SHIFT)
               {
                    shiftKeyDown = false;
               }
                if (e.getKeyCode() == KeyEvent.VK_SPACE)
               {
                    spaceKeyDown = false;
               }
               if (e.getKeyCode() == KeyEvent.VK_X)
               {
                    xKeyDown = false;
                    xyzKeys.set(0, 0);

keyID = "0";
               }
               if (e.getKeyCode() == KeyEvent.VK_Y)
               {
                    yKeyDown = false;
                    xyzKeys.set(1, 0);

keyID = "0";
               }
               if (e.getKeyCode() == KeyEvent.VK_Z)
               {
                    zKeyDown = false;
                    xyzKeys.set(2, 0);

keyID = "0";
               }
          }

          public void keyTyped(KeyEvent e)
          {
               //
          }
     }

     class paramButtonActionListener implements ActionListener
     {
          public void actionPerformed(ActionEvent e)
          { 
               if (e.getSource() == bAdd)
               {
                    alKb.add(Double.parseDouble(tfKb.getText()));  alKb.add(Double.parseDouble(tfR0.getText()));
                    atomName1_bonds.add(alAtomNames.get(0));  atomName2_bonds.add(alAtomNames.get(1));
                    atomType1_bonds.add(alAtomTypes.get(0));  atomType2_bonds.add(alAtomTypes.get(1));


               }
               else if (e.getSource() == bEdit)
               {

               }
               else if (e.getSource() == bCancel)
               {

               }
               else if (e.getSource() == bSave)
               {

               }
          }
     }


class cmbSelectedBondsActionListener implements ActionListener
     {
          public void actionPerformed(ActionEvent e)
          {
/*
               cmbSelectedBonds = (JComboBox)e.getSource();

               int selectedCmbNdx =  cmbSelectedBonds.getSelectedIndex();

System.out.println("selectedCmbNdx = " + selectedCmbNdx);

if ( (atomType1_bonds.size() > 0) && (atomType2_bonds.size() > 0) )
{
  System.out.println("atomType1_bonds.get(selectedCmbNdx) = " + atomType1_bonds.get(selectedCmbNdx));
  System.out.println("atomType2_bonds.get(selectedCmbNdx) = " + atomType2_bonds.get(selectedCmbNdx));
}



     if ( (atomType1_bonds.size() > 0) && (atomType2_bonds.size() > 0) )
     {
               tfAtomTypes.setText(atomType1_bonds.get(selectedCmbNdx) + " - " + atomType2_bonds.get(selectedCmbNdx));
     }
*/

          
          }
     }

     class elemListActionListener implements ActionListener
     {
          public void actionPerformed(ActionEvent e)
          {
               double mbX, mbY;

               mbHeight = structureMenu.getHeight();
               mbWidth = structureMenu.getWidth();

               mbx = (int)(1.0*mbWidth);
               mby = (int)(-0.2*mbHeight);

               mbx = 150;  mby = 150;

               elementList = (JComboBox)e.getSource();

               elementSelected = ((String)elementList.getSelectedItem()).trim();
 
               elemField.setText(" ");
               newColor.clear();

               // Set default element selection
               selectedElem = "C"; atomnumbr = 6; nbondVectors = 4;  hybridization = "sp3";

               hybridizationPopupSubmenu.setVisible(false);
      
               if (elementSelected.equals("H"))
               {
                    selectedElem = "H"; atomnumbr = 1; nbondVectors = 1; hybridization = " ";
                    newColor.add(1.0f); newColor.add(1.0f); newColor.add(1.0f);

                    hybridizationPopupSubmenu.setVisible(false);
               }
               else if (elementSelected.equals("C"))
               {
                    selectedElem = "C"; atomnumbr = 6;
                    newColor.add(0.5098f); newColor.add(0.5098f); newColor.add(0.5098f);

                    sp3Item.setEnabled(true);  sp2Item.setEnabled(true);  spItem.setEnabled(true);
                    trigonalPlanarItem.setEnabled(false);
                    trigonalBipyramidalItem.setEnabled(false);
                    octahedralItem.setEnabled(false);
                    squarePlanarItem.setEnabled(false);

                    hybridizationPopupSubmenu.setLocation(mbx, mby);
                    hybridizationPopupSubmenu.setVisible(true); 
               }
               else if (elementSelected.equals("O"))
               {
                    selectedElem = "O"; atomnumbr = 8;
                    newColor.add(1.0f); newColor.add(0.0f); newColor.add(0.0f);

                    sp3Item.setEnabled(true);  sp2Item.setEnabled(true);  spItem.setEnabled(false);
                    trigonalPlanarItem.setEnabled(false);
                    trigonalBipyramidalItem.setEnabled(false);
                    octahedralItem.setEnabled(false);
                    squarePlanarItem.setEnabled(false);

                    hybridizationPopupSubmenu.setLocation(mbx, mby);
                    hybridizationPopupSubmenu.setVisible(true); 
               }
               else if (elementSelected.equals("N"))
               {
                    selectedElem = "N"; atomnumbr = 7;
                    newColor.add(0.0f); newColor.add(0.0f); newColor.add(1.0f);

                    sp3Item.setEnabled(true);  sp2Item.setEnabled(true);  spItem.setEnabled(true);
                    trigonalPlanarItem.setEnabled(true);
                    trigonalBipyramidalItem.setEnabled(false);
                    octahedralItem.setEnabled(false);
                    squarePlanarItem.setEnabled(false);

                    hybridizationPopupSubmenu.setLocation(mbx, mby);
                    hybridizationPopupSubmenu.setVisible(true); 
               }
               else if (elementSelected.equals("P"))
               {
                    selectedElem = "P"; atomnumbr = 15; //nbondVectors = 2; //hybridization = "sp2";
                    newColor.add(0.9137f); newColor.add(0.6118f); newColor.add(1.0f);

                    sp3Item.setEnabled(false);  sp2Item.setEnabled(false); spItem.setEnabled(false);
                    trigonalPlanarItem.setEnabled(false);
                    trigonalBipyramidalItem.setEnabled(true);
                    octahedralItem.setEnabled(false);
                    squarePlanarItem.setEnabled(false);

                    hybridizationPopupSubmenu.setLocation(mbx, mby);
                    hybridizationPopupSubmenu.setVisible(true); 
               }               
               else if (elementSelected.equals("S"))
               {
                    selectedElem = "S"; atomnumbr = 16; //nbondVectors = 2; // hybridization = "sp3";
                    newColor.add(1.0f); newColor.add(0.9216f); newColor.add(0.0f);

                    spItem.setEnabled(false);
                    trigonalPlanarItem.setEnabled(true);
                    trigonalBipyramidalItem.setEnabled(true);
                    octahedralItem.setEnabled(true);
                    squarePlanarItem.setEnabled(false);

                    hybridizationPopupSubmenu.setLocation(mbx, mby);
                    hybridizationPopupSubmenu.setVisible(true); 
               }   
               else if (elementSelected.equals("F"))
               {
                    selectedElem = "F"; atomnumbr = 9; nbondVectors = 1; hybridization = " ";
                    newColor.add(0.702f); newColor.add(1.0f); newColor.add(1.0f);

                    hybridizationPopupSubmenu.setVisible(false);
               }   
               else if (elementSelected.equals("Cl"))
               {
                    selectedElem = "Cl"; atomnumbr = 17; nbondVectors = 1; hybridization = " ";
                    newColor.add(0.702f); newColor.add(1.0f); newColor.add(0.6784f);

                    hybridizationPopupSubmenu.setVisible(false);
               }   
               else if (elementSelected.equals("Br"))
               {
                    selectedElem = "Br"; atomnumbr = 35; nbondVectors = 1; hybridization = " ";
                    newColor.add(0.8275f); newColor.add(0.3098f); newColor.add(0.2706f);

                    hybridizationPopupSubmenu.setVisible(false);
               }  
               else if (elementSelected.equals("I"))
               {
                    selectedElem = "I"; atomnumbr = 53; nbondVectors = 1; hybridization = " ";
                    newColor.add(0.6667f); newColor.add(0.3098f); newColor.add(0.2706f);

                    hybridizationPopupSubmenu.setVisible(false);
               } 
               else if (elementSelected.equals("Be"))
               {
                    selectedElem = "Be"; atomnumbr = 4; nbondVectors = 2; hybridization = " ";
                    newColor.add(0.7725f); newColor.add(1.0000f); newColor.add(0.0000f);

                    hybridizationPopupSubmenu.setVisible(false);
               } 
               else if (elementSelected.equals("B"))
               {
                    selectedElem = "B"; atomnumbr = 5; nbondVectors = 3; hybridization = "sp2";
                    newColor.add(1.0000f); newColor.add(0.7176f); newColor.add(0.7176f);

                    hybridizationPopupSubmenu.setVisible(false);
               } 

               glcanvas.repaint();
          }
     }

     class hybridizationPopupSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               hybridization = "sp3";       // default hybridization

               if ((e.getComponent()).equals(sp3Item))
               {
                    hybridization = "sp3";

                    if (selectedElem.equals("C"))
                    {
                         nbondVectors = 4;  selectedAtomNdx = 5;
                    }
                    else if (selectedElem.equals("O"))
                    {
                         nbondVectors = 2;  selectedAtomNdx = 7;
                    }
                    else if (selectedElem.equals("N"))
                    {
                         nbondVectors = 3;  selectedAtomNdx = 6;
                    }
                    else if (selectedElem.equals("S"))
                    {
                         nbondVectors = 2;  selectedAtomNdx = 15;
                    }
               }
               else if ((e.getComponent()).equals(sp2Item))
               {
                    hybridization = "sp2";

                    if (selectedElem.equals("C"))
                    {
                         nbondVectors = 3;  selectedAtomNdx = 5;
                    }
                    else if (selectedElem.equals("O"))
                    {
                         nbondVectors = 1;  selectedAtomNdx = 7;
                    }
                    else if (selectedElem.equals("N"))
                    {
                         nbondVectors = 2;  selectedAtomNdx = 6;
                    }
               }
               else if ((e.getComponent()).equals(spItem))
               {
                    hybridization = "sp";

                    if (selectedElem.equals("C"))
                    {
                         nbondVectors = 2;  selectedAtomNdx = 5;
                    }
                    else if (selectedElem.equals("N"))
                    {
                         nbondVectors = 1;  selectedAtomNdx = 5;
                    }
               }
               else if ((e.getComponent()).equals(trigonalPlanarItem))
               {
                    hybridization = "trigonalPlanar";

                    if (selectedElem.equals("N"))
                    {
                         nbondVectors = 3;  selectedAtomNdx = 5;
                    }
                    else if (selectedElem.equals("S"))
                    {
                         nbondVectors = 2;  selectedAtomNdx = 15;
                    }
               }
               else if ((e.getComponent()).equals(trigonalBipyramidalItem))
               {
                    hybridization = "trigonalBipyramidal";

                    if (selectedElem.equals("P"))
                    {
                         nbondVectors = 5;  selectedAtomNdx = 14;
                    }
                    else if (selectedElem.equals("S"))
                    {
                         nbondVectors = 4;  selectedAtomNdx = 15;
                    }
               }
               else if ((e.getComponent()).equals(octahedralItem))
               {
                    hybridization = "octrahedral";

                    if (selectedElem.equals("S"))
                    {
                         nbondVectors = 6;  selectedAtomNdx = 15;
                    }
               }
               else if ((e.getComponent()).equals(squarePlanarItem))
               {
                    hybridization = "squarePlanar";
               }

               hidePopupSubmenus();
               hidePopupMenus();

               structurePopupMenu.setVisible(false);
               hybridizationPopupSubmenu.setVisible(false);

               newBondVectors = new double[nbondVectors][nnewbvCols];
               newBondVectors = getNewBondVectors(selectedAtomNdx, nbondVectors);
          }
     }

     class editMoleculeSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               int menuoff = 1;

               editModeOn = true;
               editMode = true;
               editModeLabel.setVisible(true);

               if (bondAngleSelected)
               {
                    rotBondAngle = true;
                    rotFragment = false;

                    fragmentSelected = false;
                    dihedralAngleSelected = false;
               }
               else
               {
                    rotBondAngle = false;
                    rotFragment = true;

                    fragmentSelected = true;
               }   

               if ((e.getComponent()).equals(addHItem))
               {
                    addHydrogens();

                    editModeOn = editMode = false;
                    editModeLabel.setVisible(false);
               }
               else if ((e.getComponent()).equals(joinFragmentsItem))
               {
                    joinFragments = true;
               }
               else if ((e.getComponent()).equals(replaceAtomItem))
               {
                    replaceAtom = true;
               }
               else if ((e.getComponent()).equals(addH2OItem))
               {
                    if (nbondvectors == 0)
                    {
                         addSolventH2O = true;

                         solvateMoleculeLabel.setVisible(true);
 
                         menuoff = 1;
                    }
                    else
                    {
                         JOptionPane.showMessageDialog(glcanvas, "A partial molecular structure (a molecule with one or more bond vectors) cannot be solvated." + '\n', "Adding a water molecule to an atom", JOptionPane.PLAIN_MESSAGE);

                         addSolventH2O = false;

                         solvateMoleculeLabel.setVisible(false);

                         menuoff = 0;
                    }

                    if (editMode)
                    {
                         editModeOn = editMode = false;
                         editModeLabel.setVisible(false);
                    }
               }

               if (menuoff == 1)
               {
                    structurePopupMenu.setVisible(false);
               }
          }
     }

     class paramPopupMenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.25*mbHeight);

               if ((e.getComponent()).equals(generateAtomTypesItem))
               {
                    //int dhIndx;
                    //String strDHAtomTypes = new String();

                    if (natoms > 0)
                    {
                         parametersPopupMenu.setVisible(false);

                         glcanvas.repaint();

                         /** webservice delegation - parametrization of input file - narendra kumar */

                         parametrization atomType = new parametrization();
                         atomType.parametrizeMol2File(paramFrame, getDisplayedFile());                                          	                            
                         
                         /* Carry out atom typing on displayed molecule
                         generateAtomTypes(getDisplayedFile());
                                                                                                    
                         atomTypes.clear();
                         if (selectedForceField.equals("CGenFF"))
                         {
                              //atomTypes = getAtomTypes("cgenff_out.txt");
                              atomTypes = getAtomTypes(strFile);
                         }

                         arrDHAtomTypes = new String[ndihedrals];
                         for (j = 0; j < ndihedrals; j++)
                         {
                              strDHAtomTypes = new String();

                              for (k = 0; k < 4; k++)
                              {
                                   dhIndx = alDihedralAtoms.get((4*j) + k);

                                   strDHAtomTypes += atomTypes.get(dhIndx) + " ";
                              }

                              arrDHAtomTypes[j] = strDHAtomTypes.trim();
                         }
                         */
                         
                         /** end of code change - narendra kumar */
                    }
                    else
                    {
                         JOptionPane.showMessageDialog(glcanvas, "Please display the molecular structure for which atom types are to be generated", "No molecule displayed!", JOptionPane.PLAIN_MESSAGE);
                    }
               }
               else if ((e.getComponent()).equals(assignParamValueItem))
               {
                    // Molecule must be displayed before parameter values can be assigned (i.e., there must be at least 1 atom displayed on display canvas)
                    if (natoms > 0)
                    {
                         parametersPopupMenu.setVisible(false);

                         paramFrame.setVisible(true);
                         paramFrameVisible = true;

                         glcanvas.repaint();

                         albondID.clear();  

                         atomName1_bonds.clear();  atomName2_bonds.clear();
                         atomType1_bonds.clear();  atomType2_bonds.clear();
                    }
                    else
                    {
                         JOptionPane.showMessageDialog(glcanvas, "Please display the molecular structure for which parameters are to be assigned", "No molecule displayed!", JOptionPane.PLAIN_MESSAGE);
                    }
               }
          }
     }



     class modForceFieldSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.25*mbHeight);
//               mby = (int)(-0.1*mbHeight);
//               mby = (int)(0.5*mbHeight);

               if ((e.getComponent()).equals(charmmSubmenu))
               {
                    if (charmmSubmenu.isVisible() == false)
                    {
                         charmmPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         charmmSubmenu.setVisible(false);
                    }    
               }
          }
     }

     class forceFieldSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.25*mbHeight);
//               mby = (int)(-0.1*mbHeight);
//               mby = (int)(0.5*mbHeight);

               if ((e.getComponent()).equals(empiricalSubmenu))
               {
                    if (empiricalPopupSubmenu.isVisible() == false)
                    {
                         empiricalPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         empiricalPopupSubmenu.setVisible(false);
                    }    
               }
               else if ((e.getComponent()).equals(semiempiricalSubmenu))
               {
                    if (semiempiricalPopupSubmenu.isVisible() == false)
                    {
                         semiempiricalPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         semiempiricalPopupSubmenu.setVisible(false);
                    }    
               }
               else if ((e.getComponent()).equals(coarseGrainedSubmenu))
               {
                    if (coarseGrainedPopupSubmenu.isVisible() == false)
                    {
                         coarseGrainedPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         coarseGrainedPopupSubmenu.setVisible(false);
                    }    
               }
               else if ((e.getComponent()).equals(manyBodySubmenu))
               {
                    if (manyBodyPopupSubmenu.isVisible() == false)
                    {
                         manyBodyPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         manyBodyPopupSubmenu.setVisible(false);
                    }    
               }
               else if ((e.getComponent()).equals(specialFFSubmenu))
               {
                    if (specialFFPopupSubmenu.isVisible() == false)
                    {
                         specialFFPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         specialFFPopupSubmenu.setVisible(false);
                    }    
               }
                else if ((e.getComponent()).equals(userDefinedSubmenu))
               {
                    if (userDefinedPopupSubmenu.isVisible() == false)
                    {
                         userDefinedPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         userDefinedPopupSubmenu.setVisible(false);
                    }    
               }
          }
     }

     class empiricalSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.25*mbHeight);

               if ((e.getComponent()).equals(class1additiveSubmenu))
               {
                    if (class1additivePopupSubmenu.isVisible() == false)
                    {
                         class1additivePopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         class1additivePopupSubmenu.setVisible(false);
                    }  
               }
          }
     }

     class class1SubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.25*mbHeight);

               JLabel ffParameters;

               String capSigma, addStr, bondStr, angleStr, dihedralStr, impropersStr, UreyBradleyStr, electroStr, vdwStr, ffParamLabel;
               String thetaUC, phiUC, epsilonUC; 

               addStr = "  &nbsp+&nbsp  ";
               // Unicode for Greek letters
               //   theta:             \u03B8
               //   uppercase phi:     \u03A6
               //   lowercase phi:     \u03C6
               //   delta:             \u03B4
               //   pi:                \u03C0
               //   epsilon:           \u03B5
               capSigma = " <font size=6> \u03A3</font><font size=4>";

               selectedForceField = new String();

               boolean charmmSelected = false;  boolean oplsSelected = false;

               forcefieldSelected = false;

//               selectedForceField = null;

               // Populate strings for each force field term
                    bondStr = "<font size=4> k<sub>b</sub> (r<sub>i</sub> - r<sub>0</sub>)<sup>2</sup></font>";
                    angleStr = "<font size=4> k<sub>\u03B8</sub> (\u03B8<sub>i</sub> - \u03B8<sub>0</sub>)<sup>2</sup></font>";
                    dihedralStr = "<font size=4> k<sub>\u03A6</sub> ( 1 + cos(n\u03A6 - \u03B4) )";
                    impropersStr = "<font size=4> k<sub>\u03C6</sub> (\u03C6<sub>i</sub> - \u03C6<sub>0</sub>)<sup>2</sup></font>";
                    UreyBradleyStr = "<font size=4> k<sub>UB</sub> ( (r<sub>1, 3</sub>) - (r<sub>1, 3</sub>)<sub>0</sub></sub>)<sup>2</sup></font>";
                    electroStr = "<font size=4> q<sub>i</sub>q<sub>j</sub>/4\u03C0Dr<sub>ij</sub></font>";
                    vdwStr = "<font size=4> \u03B5<sub>ij</sub> [ (R<sub>min, ij</sub>/r<sub>ij</sub>)<sup>12</sup> - 2(R<sub>min, ij</sub>/r<sub>ij</sub>)<sup>6</sup> ]";

               // Add force field terms to JLabel
                    ffParamLabel = "<html>" + capSigma + bondStr + addStr + capSigma + angleStr + addStr;
                    ffParamLabel = ffParamLabel + capSigma + dihedralStr + "<br>";
                    ffParamLabel = ffParamLabel + addStr + capSigma + impropersStr + addStr + capSigma + UreyBradleyStr + "<br>";
                    ffParamLabel = ffParamLabel + addStr + capSigma + electroStr + addStr + capSigma + vdwStr + "<br><br></html>";

                    ffParameters = new JLabel(ffParamLabel, JLabel.CENTER);

               if ((e.getComponent()).equals(amberItem))
               {
                    charmmPopupSubmenu.setVisible(false);
                    oplsPopupSubmenu.setVisible(false);

                    Object[] FFselections = {"Amber force field"};

                    selectedForceField = "Amber";

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of Amber force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(charmmSubmenu))
               {
                    charmmSelected = true;

                    if (charmmPopupSubmenu.isVisible() == false)
                    {
                         oplsPopupSubmenu.setVisible(false);

                         charmmPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         charmmPopupSubmenu.setVisible(false);
                    }  
               }
               else if ((e.getComponent()).equals(allAtomItem))
               {
                    Object[] FFselections = {"CHARMM All Atom force field"};

                    selectedForceField = "CharmmAA";

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of CHARMM All Atom force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(unitedAtomItem))
               {
                    Object[] FFselections = {"CHARMM United Atom force field"};

                    selectedForceField = "CharmmUA";

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of CHARMM United Atom force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(cgenffItem))
               {
                    Object[] FFselections = {"CGenFF force field"};

                    selectedForceField = "CGenFF";

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of CGenFF force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(cvffItem))
               {
                    charmmPopupSubmenu.setVisible(false);
                    oplsPopupSubmenu.setVisible(false);

                    Object[] FFselections = {"CVFF force field"};

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of CVFF force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(eceppItem))
               {
                    charmmPopupSubmenu.setVisible(false);
                    oplsPopupSubmenu.setVisible(false);

                    Object[] FFselections = {"ECEPP force field"};

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of ECEPP force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(gromacsItem))
               {
                    charmmPopupSubmenu.setVisible(false);
                    oplsPopupSubmenu.setVisible(false);

                    Object[] FFselections = {"Gromacs force field"};

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of Gromacs force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(gromosItem))
               {
                    charmmPopupSubmenu.setVisible(false);
                    oplsPopupSubmenu.setVisible(false);

                    Object[] FFselections = {"Gromos force field"};

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of Gromos force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }
               else if ((e.getComponent()).equals(oplsSubmenu))
               {
                    oplsSelected = true;

                    if (oplsPopupSubmenu.isVisible() == false)
                    {
                         charmmPopupSubmenu.setVisible(false);

                         oplsPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         oplsPopupSubmenu.setVisible(false);
                    }  
               }
               else if ((e.getComponent()).equals(saapItem))
               {
                    charmmPopupSubmenu.setVisible(false);
                    oplsPopupSubmenu.setVisible(false);

                    Object[] FFselections = {"SAAP force field"};

                    selectedFF = JOptionPane.showInputDialog(glcanvas, ffParameters, "Parametrization of SAAP force field", JOptionPane.PLAIN_MESSAGE, null, FFselections, FFselections[0]);
               }

               if ( (!(e.getComponent()).equals(charmmSubmenu)) && (!(e.getComponent()).equals(oplsSubmenu)) )
               {
                    hidePopupMenus();
                    hidePopupSubmenus();
               }

               if ( (selectedFF == null) || (selectedForceField == null) )
               {
                    forceFieldPopupMenu.setVisible(true);
                    class1additivePopupSubmenu.setVisible(true);
                    empiricalPopupSubmenu.setVisible(true);

                    if (charmmSelected)
                    {
                         charmmPopupSubmenu.setVisible(true);
                    }
                    else if (oplsSelected)
                    {
                         oplsPopupSubmenu.setVisible(true);
                    }
               }
          }
     }

     class oplsSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               hidePopupMenus();  hidePopupSubmenus();

               //
          }
     }

     class textFieldListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               if ((e.getComponent()).equals(bondLengthField))
               {
                    attributeModified = 1;

                    initBondLngth = bondLngth;
                    bondLengthTxtFldSelected = true;
               }
               else if ((e.getComponent()).equals(bondAngleField))
               {
                    attributeModified = 2;

                    initBondAngle = bondAngle;
                    bondAngleTxtFldSelected = true; 
               }
               else if ((e.getComponent()).equals(dihedralAngleField))
               {
                    attributeModified = 3;
                    
                    initDihedralAngle = dihedralAngle;
                    dihedralTxtFldSelected = true;
               }
               else if ((e.getComponent()).equals(xCoordField))
               {
                    attributeModified = 5;
               }
               else if ((e.getComponent()).equals(yCoordField))
               {
                    attributeModified = 5;
               }
               else if ((e.getComponent()).equals(zCoordField))
               {
                    attributeModified = 5;
               }
          }
     }

     class fcnalGrpSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               int ninitatoms, nbondvecs;
               filename = new String();
               String fragType = "fcnalGrp";     // functional group file

               x0 = y0 = z0 = 0.0;

               ninitatoms = natoms;

               if (natoms == 0)
               {
                    theta = phi = psi = 0.0;
               }

               displayMode = true;
               displayStructure = false;
               displayFragment = true;

               drawMode = true;
               addFcnalGrp = true;  
               addElement = addFragment = false;

               nbondvecs = 0;

               if ((e.getComponent()).equals(aminoItem))
               {
                    filename = new String(functionalGroupFileDir + "amino.mol2");
                    nbondvecs = 1;
               }
               else if ((e.getComponent()).equals(benzothiazolylItem))
               {
                    filename = new String(functionalGroupFileDir + "benzothiazolyl.mol2");
                    nbondvecs = 1;
               }
               else if ((e.getComponent()).equals(carboxylItem))
               {
                    filename = new String(functionalGroupFileDir + "carboxylicAcid.mol2");
                    nbondvecs = 1;
               }
               else if ((e.getComponent()).equals(esterItem))
               {
                    filename = new String(functionalGroupFileDir + "ester.mol2");
                    nbondvecs = 2;
               }
               else if ((e.getComponent()).equals(formylItem))
               {
                    filename = new String(functionalGroupFileDir + "formyl.mol2");
                    nbondvecs = 1;
               }
               else if ((e.getComponent()).equals(hydroxylItem))
               {
                    filename = new String(functionalGroupFileDir + "hydroxyl.mol2");
                    nbondvecs = 1;
               }
               else if ((e.getComponent()).equals(ketoneItem))
               {
                    filename = new String(functionalGroupFileDir + "ketone.mol2");
                    nbondvecs = 2;
               }
               else if ((e.getComponent()).equals(methylItem))
               {
                    filename = new String(functionalGroupFileDir + "methyl.mol2");
                    nbondvecs = 1;
               }
               else if ((e.getComponent()).equals(phenylItem))
               {
                    filename = new String(functionalGroupFileDir + "phenyl.mol2");
                    nbondvecs = 1;
               }
               else if ((e.getComponent()).equals(sulfhydrylItem))
               {
                    filename = new String(functionalGroupFileDir + "sulfhydryl.mol2");
                    nbondvecs = 1;
               }

//               str = "\\";
//               fileSepNdx = filename.lastIndexOf(str);
               fileSepNdx = filename.lastIndexOf(fsep);
               str = ".";
               dotExtNdx = filename.lastIndexOf(str);

               moleculeName = filename.substring((fileSepNdx + 1), dotExtNdx);

               // Read selected file 
               try
               {
//                    readFragmentFile(filename, nbondvecs, fragType);
                    readFragmentFile(filename, fragType);

                    displayedFile = filename;

                    inputFileRead = true;
               } 
               catch (FileNotFoundException ex)
               {
                    inputFileRead = false;

                    System.out.println("Error:  " + filename + " not found");
               }

               hidePopupMenus();
               hidePopupSubmenus();

               if (ninitatoms == 0)
               {
                    int userDefFileNumbr;
                    String infilename, mol2filename;

                    userDefFileSuffix = 0;

//                    generateMol2File("New Molecule");
                    generateMol2File(moleculeName);

//                    mol2filename = "userDefinedMolecule";          
                    mol2filename = moleculeName;    

                    // Add file number extension
                    userDefFileNumbr = getUserDefFileSuffix();

                    userDefFileNumbr = userDefFileSuffix + 1000;

                    mol2filename = mol2filename + Integer.toString(userDefFileNumbr);

//                    generateAtomTypes(userDefStructureDir + mol2filename + ".mol2");
//                    atomTypes.clear();
//                    atomTypes = getAtomTypes("cgenff_out.txt");

                    displayStructure = true;

                    selectedNames = getSelectedNames();
               }

               selectMode = 1;
               glcanvas.repaint();                
          }
     }

     class fragmentSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               filename = new String();
               String fragType = "fragment";     // molecular fragment

               x0 = y0 = z0 = 0.0;
               theta = phi = psi = 0.0;

               displayMode = true;
               displayStructure = false;
               displayFragment = true;

               drawMode = true;
               addFragment = true;  
               addElement = addFcnalGrp = false;

               nbondvectors = 0;

               if ((e.getComponent()).equals(ammoniaItem))
               {
                    filename = new String(molecularFragmentFileDir + "ammonia.mol2");
               }              
               else if ((e.getComponent()).equals(benzeneItem))
               {
                    filename = new String(molecularFragmentFileDir + "benzene.mol2");
               }
               else if ((e.getComponent()).equals(benzothiazole2Item))
               {
                    filename = new String(molecularFragmentFileDir + "benzothiazole.mol2");
               }
               else if ((e.getComponent()).equals(formaldehydeItem))
               {
                    filename = new String(molecularFragmentFileDir + "formaldehyde.mol2");
               }
               else if ((e.getComponent()).equals(formicAcidItem))
               {
                    filename = new String(molecularFragmentFileDir + "formicAcid.mol2");
               }
               else if ((e.getComponent()).equals(furanItem))
               {
                    filename = new String(molecularFragmentFileDir + "furan.mol2");
               }
               else if ((e.getComponent()).equals(hydrogenSulfideItem))
               {
                    filename = new String(molecularFragmentFileDir + "H2S.mol2");
               }
               else if ((e.getComponent()).equals(methaneItem))
               {
                    filename = new String(molecularFragmentFileDir + "methane.mol2");
               }
               else if ((e.getComponent()).equals(pyridineItem))
               {
                    filename = new String(molecularFragmentFileDir + "pyridine.mol2");
               }
               else if ((e.getComponent()).equals(pyrroleItem))
               {
                    filename = new String(molecularFragmentFileDir + "pyrrole.mol2");
               }
               else if ((e.getComponent()).equals(thiazoleItem))
               {
                    filename = new String(molecularFragmentFileDir + "thiazole.mol2");
               }
               else if ((e.getComponent()).equals(thiopheneItem))
               {
                    filename = new String(molecularFragmentFileDir + "thiophene.mol2");
               }
               else if ((e.getComponent()).equals(waterItem))
               {
                    filename = new String(molecularFragmentFileDir + "water.mol2");
               }

//               str = "\\";
//               fileSepNdx = filename.lastIndexOf(str);
               fileSepNdx = filename.lastIndexOf(fsep);

               str = ".";
               dotExtNdx = filename.lastIndexOf(str);

               moleculeName = filename.substring((fileSepNdx + 1), dotExtNdx);

               // Read selected file
               try
               {
//                    readFragmentFile(filename, nbondvectors, fragType);
                    readFragmentFile(filename, fragType);

                    displayedFile = filename;

                    inputFileRead = true;
               } 
               catch (FileNotFoundException ex)
               {
                    inputFileRead = false;

                    System.out.println("Error:  " + filename + " not found");
               }

               hidePopupSubmenus();
               hidePopupMenus();

               selectMode = 1;
               glcanvas.repaint();                
          }
     }


     class paramTableListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               if ((e.getComponent()).equals(displayAtomTypeTableItem))
               {

               }
               else if ((e.getComponent()).equals(displayBondParametersTableItem))
               {

               }
               else if ((e.getComponent()).equals(displayDihedralParametersTableItem))
               {
                    dhParametersTableFrame.setVisible(true);

                    hidePopupMenus();  hidePopupSubmenus();
               }
          }
     }

     class displaySubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               setStructureMenuEnabledStatus(true);

               int returnVal;
               String fname;
               File currDir;

               fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = (int)(1.0*mbWidth);  mby = (int)(-0.2*mbHeight);

               if ((e.getComponent()).equals(displayAminoAcidSubmenu))
               {
                    hidePopupMenus();  hidePopupSubmenus();

                    currDir = new File(paramchemDir + "Structures" + fsep + "Molecules" + fsep + "aminoAcids" + fsep);
                    fc.setCurrentDirectory(currDir);
               }
               if ((e.getComponent()).equals(displayDrugSubmenu))
               {
                    hidePopupMenus();  hidePopupSubmenus();

                    currDir = new File(paramchemDir + "Structures" + fsep + "Molecules" + fsep + "drugs" + fsep);
                    fc.setCurrentDirectory(currDir);

               }
               if ((e.getComponent()).equals(displayLipidSubmenu))
               {
                    hidePopupMenus();  hidePopupSubmenus();

                    currDir = new File(paramchemDir + "Structures" + fsep + "Molecules" + fsep + "lipids" + fsep);
                    fc.setCurrentDirectory(currDir);
               }
               if ((e.getComponent()).equals(displayNucleicAcidSubmenu))
               {
                    hidePopupMenus();  hidePopupSubmenus();

                    currDir = new File(paramchemDir + "Structures" + fsep + "Molecules" + fsep + "nucleicAcids" + fsep);
                    fc.setCurrentDirectory(currDir);
               }
               if ((e.getComponent()).equals(displaySugarSubmenu))
               {
                    hidePopupMenus();  hidePopupSubmenus();

                    currDir = new File(paramchemDir + "Structures" + fsep + "Molecules" + fsep + "sugars" + fsep);
                    fc.setCurrentDirectory(currDir);
               }

               if ((e.getComponent()).equals(displayCofactorSubmenu))
               {
                    hidePopupMenus();  hidePopupSubmenus();

                    currDir = new File(paramchemDir + "Structures" + fsep + "Molecules" + fsep + "cofactors" + fsep);
                    fc.setCurrentDirectory(currDir);
               }

               if ((e.getComponent()).equals(displayVitaminSubmenu))
               {
                    hidePopupMenus();  hidePopupSubmenus();

                    currDir = new File(paramchemDir + "Structures" + fsep + "Molecules" + fsep + "vitamins" + fsep);
                    fc.setCurrentDirectory(currDir);
               }               

               returnVal = fc.showOpenDialog(glcanvas);

               if (returnVal == JFileChooser.APPROVE_OPTION)  
               {
                    fname = fc.getSelectedFile().getPath(); 

                    // Get name of molecule including .mol2 file extension  
                    moleculeName = fc.getSelectedFile().getName();

                    // Remove '.mol2' file extension from molecule name  
                    fileSepNdx = moleculeName.lastIndexOf(fsep);

                    str = ".";
                    dotExtNdx = moleculeName.lastIndexOf(str);

                    moleculeName = moleculeName.substring((fileSepNdx + 1), dotExtNdx);

                    // Read selected file
                    try
                    {
                         readCoordsFile(fname);

                         displayedFile = fname;

                         inputFileRead = true;
                    } 
                    catch (FileNotFoundException ex)
                    {
                         inputFileRead = false;

                         System.out.println("Error:  " + fname + " not found");
                    }
               }

               deleteSelectedAttributes();

               glcanvas.repaint();
          }
     }

     class menuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = (int)(0.5*mbWidth);
               mby = (int)(0.95*mbHeight);

               // Hide any popup submenus that are visible (hide popup menus when select popup submenu component)
               hidePopupSubmenus();

               // Hide paramFrame until parametersMenu selected
               paramFrameVisible = false;

               if ((e.getComponent()).equals(fileMenu))
               {
                    hidePopupMenus();

                    if (filePopupMenu.isVisible() == false)
                    {
                         filePopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         filePopupMenu.setVisible(false);
                    }          
               }
               else if ((e.getComponent()).equals(editMenu))
               {
                    hidePopupMenus();

                    if (editPopupMenu.isVisible() == false)
                    {
                         editPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         editPopupMenu.setVisible(false);
                    }          
               }
               else if ((e.getComponent()).equals(viewMenu))
               {
                    hidePopupMenus();

                    if (viewPopupMenu.isVisible() == false)
                    {
                         viewPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         viewPopupMenu.setVisible(false);
                    }          
               }
               else if ((e.getComponent()).equals(structureMenu))
               {
                    hidePopupMenus();

                    if (structurePopupMenu.isVisible() == false)
                    {
                         structurePopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         structurePopupMenu.setVisible(false);
                    }          
               }
               else if ((e.getComponent()).equals(displayMenu))
               {
                    hidePopupMenus();

                    if (displayPopupMenu.isVisible() == false)
                    {
                         displayPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         displayPopupMenu.setVisible(false);
                    }       
               }
               else if ((e.getComponent()).equals(forceFieldMenu))
               {
                    hidePopupMenus();

                    // Check to see if user has already selected a force field
                    if (selectedFF != null) 
                    { 
                         Object[] selectionOptions = { "Yes", "No", "Cancel" };
 
                         int response = JOptionPane.showOptionDialog(glcanvas, "          The " + selectedForceField + " force field has already been selected." + '\n' + '\n' + "Do you want to replace this selection with an alternative force field?" + '\n' + '\n', "Force field selection", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, selectionOptions, selectionOptions[0]);
                         if (response == 0)                           // If response is 'Yes', allow user to selected an alternative force field
                         {
                              selectedFF = null;  
                              selectedForceField = null;

                              JOptionPane.showMessageDialog(glcanvas, "If you have already assigned parameters for the force field that you are replacing," + '\n' + "        be sure to reassign those parameters for the new force field you select.", "Assignment of parameters to new force field", JOptionPane.PLAIN_MESSAGE);
                         }
                         else if (response == 1)                      // If response is 'No', do nothing
                         {
                              //
                         }
                    }

                    if (selectedFF == null)
                    {
                         if (forceFieldPopupMenu.isVisible() == false)
                         {
                              forceFieldPopupMenu.show(e.getComponent(), mbx, mby);
                         }                    
                         else 
                         {
                              forceFieldPopupMenu.setVisible(false);
                         }    

                    }
               }
               else if ((e.getComponent()).equals(parametersMenu))
               {
                    hidePopupMenus();

//                    if (selectedFF == null)
//                    {
//                        JOptionPane.showMessageDialog(glcanvas, "Please select a force field before selecting parameters", "No force field defined", JOptionPane.PLAIN_MESSAGE);
//                    }
//                    else
//                    {
                         if (parametersPopupMenu.isVisible() == false)
                         {
                              parametersPopupMenu.show(e.getComponent(), mbx, mby);
                         }                    
                         else 
                         {
                              parametersPopupMenu.setVisible(false);
                         }    
//                    }
               }
               else if ((e.getComponent()).equals(runMenu))
               {
                    hidePopupMenus();

                    if (runPopupMenu.isVisible() == false)
                    {
                         runPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         runPopupMenu.setVisible(false);
                    }    
               }
               else if ((e.getComponent()).equals(settingsMenu))
               {
                    hidePopupMenus();
               }
               else if ((e.getComponent()).equals(helpMenu))
               {
                    hidePopupMenus();

                    if (helpPopupMenu.isVisible() == false)
                    {
                         helpPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         helpPopupMenu.setVisible(false);
                    }          
               }
          }
     }

     class runSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {     

/*
               if ((e.getComponent()).equals(generateAtomTypesItem_02))
               {
                    if (natoms == 0)
                    {
                         JOptionPane.showMessageDialog(glcanvas, "Please display the molecular structure for which atom types are to be generated", "No molecule displayed!", JOptionPane.PLAIN_MESSAGE);
                    }
                    else
                    {
                         hidePopupMenus();

           // connect to remote Paramcad server, send mol2 file, and retrieve cgenff.exe output

                         glcanvas.repaint();
                    }                   
               }
*/

//               else if ((e.getComponent()).equals(dihedralOptimizationItem))
               if ((e.getComponent()).equals(dihedralOptimizationItem))
               {


String newText;

newText = getDHAtmTypTxtArea();

dhAtmTypTxtArea.setText(null);
dhAtmTypTxtArea.setText(newText);


                    if (natoms == 0)
                    {
                         JOptionPane.showMessageDialog(glcanvas, "Please display the molecular structure for which dihedrals are to be optimized", "No molecule displayed!", JOptionPane.PLAIN_MESSAGE);
                    }
                    else if (atomTypes.size() == 0)
                    {
                         JOptionPane.showMessageDialog(glcanvas, "Initial guess for this molecule must first be generated before dihedral parameters can be optimized", "Atom types have not been generated!", JOptionPane.PLAIN_MESSAGE);

                         hidePopupMenus();

                         glcanvas.repaint();
                    }
                    else
                    {
                         hidePopupMenus();

                         dhOptFrame.setVisible(true);
 
                         glcanvas.repaint();
                    }                   
               }
          }
     }


     class submenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.5*mbHeight);
          }
     }

     class importSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               if (importSubmenu.contains(e.getX(), e.getY()))
               {
                    if (importPopupSubmenu.isVisible() == false)
                    {
                         importPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (importPopupSubmenu.isVisible() == true)
                         {
                              importPopupSubmenu.setVisible(false);
                         }
                    }       

                    exportPopupSubmenu.setVisible(false);
               }
          }
     }      

     class exportSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               if (exportSubmenu.contains(e.getX(), e.getY()))
               {
                    if (exportPopupSubmenu.isVisible() == false)
                    {
                         exportPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (exportPopupSubmenu.isVisible() == true)
                         {
                              exportPopupSubmenu.setVisible(false);
                         }
                    }       

                    importPopupSubmenu.setVisible(false);
               }
          }
     }      

     class projectionSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.35*mbHeight);

               if (projectionSubmenu.contains(e.getX(), e.getY()))
               {
                    if (projectionPopupSubmenu.isVisible() == false)
                    {
                         projectionPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (projectionPopupSubmenu.isVisible() == true)
                         {
                              projectionPopupSubmenu.setVisible(false);
                         }
                    }       
               }
          }
     }      

     class gaussianImportMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               // Set importStructureFile = true after file is imported
          }
     }

     class gamessImportMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               // Set importStructureFile = true after file is imported
          }
     }

     class gaussianExportMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {

          }
     }

     class gamessExportMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {

          }
     }


//////////////
///////////////

     class fileSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {     
               if ((e.getComponent()).equals(openItem))
               {

               }
               else if ((e.getComponent()).equals(saveAsItem))
               {
                    int returnVal, actionDialog;
                    String savedFilename;
                    File currDir, fileName;
                    

                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                    hidePopupSubmenus();
                    hidePopupMenus();

                    returnVal = fc.showSaveDialog(glcanvas);
//                    actionDialog = fc.showSaveDialog(glcanvas);

                    if (returnVal == JFileChooser.CANCEL_OPTION) 
                    {
                         return;      
                    }

                    else if (returnVal == JFileChooser.APPROVE_OPTION)  
                    {
                         savedFilename = fc.getSelectedFile().getPath(); 

                         fileName = new File(fc.getSelectedFile().getPath()); 

                         if(fileName.exists())
                         {
                              actionDialog = JOptionPane.showConfirmDialog(glcanvas, "Replace existing file?");

                              if ( (actionDialog == JOptionPane.NO_OPTION) || (actionDialog == JOptionPane.CANCEL_OPTION) )
                              {
                                  return;     // do not save file
                              }
//                              else if (actionDialog == JOptionPane.YES_OPTION)
//                              {

//                              }
                         }

                         savedFilename = fc.getSelectedFile().getPath(); 

System.out.println("savedFilename = " + savedFilename);
  
                         writeNewMol2File(savedFilename);
                    }
               }
          }
     }

     class editSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {     
               int userDefFileNumbr, userDefnbvNdx;
               String infilename;
               File srcfile, destfile;      


               if ((e.getComponent()).equals(undoItem))
               {
                    if (natoms > 0)
                    {
                         elementList.setVisible(false);

                         undo = true;  redo = false;

                         userDefFileSuffix = getUserDefFileSuffix();

                         if (userDefFileSuffix > 1)
                         {
                              userDefFileSuffix--;

                              userDefFileNumbr = userDefFileSuffix + 1000;

                              infilename = moleculeName + Integer.toString(userDefFileNumbr) + ".mol2";

                              // Add path directory
//                              infilename = tmpUserDefStructureDir + "\\" + infilename;
                              infilename = tmpUserDefStructureDir + fsep + infilename;

                              srcfile = new File(infilename);
                              destfile = new File(userDefStructureDir + moleculeName + ".mol2");

                              // Copy file from tmpUserDefStructure directory to User Defined Stucture directory
                              copyFile2File(srcfile, destfile); 

                              userDefnbvNdx = userDefFileSuffix - 1;

                              clearDisplay();

                              // Read selected file
                              try
                              {
                                   readCoordsFile(infilename);

                                   inputFileRead = true;
                              } 
                              catch (FileNotFoundException ex)
                              {
                                   inputFileRead = false;
   
                                   System.out.println("Error:  " + filename + " not found");
                              }       
                         } 
                         else if (userDefFileSuffix == 1)
                         {
                              undo = false;
                         }
                    }
               }
               else if ((e.getComponent()).equals(redoItem))
               {
                    undo = false;  redo = false;

                    if (natoms > 0)
                    {
                         elementList.setVisible(false);

                         redo = true;  undo = false;

                         userDefFileNumbr = getUserDefFileSuffix();               

                         if (userDefFileSuffix < userDefnbvs.size())
                         {
                              userDefFileSuffix++;

                              userDefFileNumbr = userDefFileSuffix + 1000;

                              infilename = moleculeName + Integer.toString(userDefFileNumbr) + ".mol2";

                              // Add path directory
//                              infilename = tmpUserDefStructureDir + "\\" + infilename;
                              infilename = tmpUserDefStructureDir + fsep + infilename;

                        
                              srcfile = new File(infilename);
                              destfile = new File(userDefStructureDir + moleculeName + ".mol2");

                              // Copy file from tmpUserDefStructure directory to User Defined Stucture directory
                              copyFile2File(srcfile, destfile); 

                              userDefnbvNdx = userDefFileSuffix - 1;

                              clearDisplay();

                              // Read selected file
                              try
                              {
                                   readCoordsFile(infilename);

                                   inputFileRead = true;
                              } 
                              catch (FileNotFoundException ex)
                              {
                                   inputFileRead = false;
   
                                   System.out.println("Error:  " + filename + " not found");
                              }       

                         }
                         else if (userDefFileSuffix == userDefnbvs.size())
                         {
                              redo = false;
                         }
                    }
               }
               else if ((e.getComponent()).equals(pasteItem))
               {
                    // 
               }

               hidePopupMenus();
               hidePopupSubmenus();

               if ( (natoms > 0) && (userDefFileSuffix > 0) )
               {
                    theta = usrdefTheta.get( (userDefFileSuffix - 1) );  
                    phi = usrdefPhi.get( (userDefFileSuffix - 1) );  
                    psi = usrdefPsi.get( (userDefFileSuffix - 1) ); 
               }

               selectMode = 1;
               glcanvas.repaint();
          }
     }

     class viewSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.35*mbHeight);

               // Hide any popup submenus that are visible
               hidePopupSubmenus();

               if ((e.getComponent()).equals(displayStyleSubmenu))
               {
                    editPopupMenu.setVisible(false);
                    structurePopupMenu.setVisible(false);
                    helpPopupMenu.setVisible(false);

                    if (displayStylePopupSubmenu.isVisible() == false)
                    {
                         displayStylePopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         displayStylePopupSubmenu.setVisible(false);
                    }   
               }
               else if ((e.getComponent()).equals(projectionSubmenu))
               {
                    editPopupMenu.setVisible(false);
                    structurePopupMenu.setVisible(false);
                    helpPopupMenu.setVisible(false);

                    if (projectionPopupSubmenu.isVisible() == false)
                    {
                         projectionPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         projectionPopupSubmenu.setVisible(false);
                    }          
               }
          }
     }

     class viewPopupSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               if ((e.getComponent()).equals(orthographicItem))
               {
                    //
               }
          }
     }

     class displayStylePopupSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               if ((e.getComponent()).equals(lineItem))
               {
                    displayStyle = "line";

                    glcanvas.repaint();
               }
               else if ((e.getComponent()).equals(ball_and_stickItem))
               {                    
                    displayStyle = "ball+stick";
                    bondsColored = false;

                    glcanvas.repaint();
               }

               hidePopupMenus();
               hidePopupSubmenus();

               glcanvas.repaint();
          }          
     }

     class createMoleculeSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               if (createMoleculeSubmenu.contains(e.getX(), e.getY()))
               {
                    if (createMoleculePopupSubmenu.isVisible() == false)
                    {
                         createMoleculePopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (createMoleculePopupSubmenu.isVisible() == true)
                         {
                              createMoleculePopupSubmenu.setVisible(false);
                         }
                    }       
               }

               if ((e.getComponent()).equals(newStuctureItem))
               {
                    moleculeName = JOptionPane.showInputDialog(glcanvas, "Name of Molecule:");

                    if (moleculeName != null)
                    {
                         if (moleculeName.length() > 0) 
                         {
                              setStructureMenuEnabledStatus(true);
                         }
                         else
                         {
                              structurePopupMenu.setVisible(false);
                         }
                    }
                    else
                    {
                         structurePopupMenu.setVisible(false);
                    }                    
               }
          }
     }      

     class addAtomBuildMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               drawMode = true;

               hidePopupSubmenus();
               hidePopupMenus();
          }
     }

     class drawAtomMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               if (drawAtomItem.contains(e.getX(), e.getY()))

               {
                    elementList.setVisible(true);
                    elementList.setSelectedIndex(1);

                    drawMode = true;
                    addElement = true; 
                    addFragment = addFcnalGrp = false;

                    hidePopupSubmenus();
                    hidePopupMenus();
               }
          }
     }      

     class hybridizationSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               if (hybridizationSubmenu.contains(e.getX(), e.getY()))
               {
                    if (hybridizationPopupSubmenu.isVisible() == false)
                    {
                         hybridizationPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         hybridizationPopupSubmenu.setVisible(false);
                    }          
               }
          } 
     }

     class drawFcnalGrpMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               int returnVal;
               String fname, fragType;
               File currDir;

               fragType = "fcnalGrp";

               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               elementList.setVisible(false);

               if (drawFcnalGrpItem.contains(e.getX(), e.getY()))
               {
                    drawMode = true;
//                    addFcnalGrp = true;  
//                    addElement = addFragment = false;

/*
                    if (drawFcnalGrpPopupSubmenu.isVisible() == false)
                    {
                         drawFcnalGrpPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (drawFcnalGrpPopupSubmenu.isVisible() == true)
                         {
                              drawFcnalGrpPopupSubmenu.setVisible(false);
                         }
                    }       
*/

                    currDir = new File(strFunctionalGroupFileDir);
                    fc.setCurrentDirectory(currDir);

                    returnVal = fc.showOpenDialog(glcanvas);

                    if (returnVal == JFileChooser.APPROVE_OPTION)  
                    {
                         fname = fc.getSelectedFile().getPath(); 

                         // Read selected file
                         try
                         {
//                              readFragmentFile(fname, 1, fragType);
                              readFragmentFile(fname, fragType);


                              displayedFile = fname;

                              inputFileRead = true;
                         } 
                         catch (FileNotFoundException ex)
                         {
                              inputFileRead = false;

                              System.out.println("Error:  " + fname + " not found");
                         }
                    }

                    hidePopupSubmenus();
                    hidePopupMenus();
               }  
          }
     }      

//     class drawFragmentSubmenuMouseListener extends MouseAdapter
class drawFragmentMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               int returnVal;
               String fname, fragType;
               File currDir;

               fragType = "fragment";

               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               elementList.setVisible(false);

               if (drawFragmentItem.contains(e.getX(), e.getY()))
               {
                    drawMode = true;
                    addFragment = true;  
                    addElement = addFcnalGrp = false;

/*
                    hidePopupSubmenus();

                    if (drawFragmentPopupSubmenu.isVisible() == false)
                    {
                         drawFragmentPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (drawFragmentPopupSubmenu.isVisible() == true)
                         {
                              drawFragmentPopupSubmenu.setVisible(false);
                         }
                    }       
*/

                    currDir = new File(strMolecularFragmentFileDir);
                    fc.setCurrentDirectory(currDir);

                    returnVal = fc.showOpenDialog(glcanvas);

                    if (returnVal == JFileChooser.APPROVE_OPTION)  
                    {
                         fname = fc.getSelectedFile().getPath(); 

                         // Read selected file
                         try
                         {
//                              readFragmentFile(fname, 1, fragType);
                              readFragmentFile(fname, fragType);


                              displayedFile = fname;

                              inputFileRead = true;
                         } 
                         catch (FileNotFoundException ex)
                         {
                              inputFileRead = false;

                              System.out.println("Error:  " + fname + " not found");
                         }
                    }

                    hidePopupSubmenus();
                    hidePopupMenus();
               }
          }
     }      

     class createNanostructureSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(-0.1*mbHeight);

               hidePopupSubmenus();

               if (createNanostructureSubmenu.contains(e.getX(), e.getY()))
               {
                    if (createNanostructurePopupSubmenu.isVisible() == false)
                    {
                         createNanostructurePopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (createNanostructurePopupSubmenu.isVisible() == true)
                         {
                              createNanostructurePopupSubmenu.setVisible(false);
                         }
                    }       
               }
          }
     }      

     class dhParametersTableListener extends MouseAdapter
     {
          int selectedRowNdx, selectedColNdx, alIndex;
     int selectedDHParamtersTableCol;
//          int[] dhRowsSelected;

          String  strSelectedDihedrals;

          ArrayList<Integer> alRowndx = new ArrayList<Integer>();

          public void mousePressed(MouseEvent e)
          {  
               JTable tableSelection = (JTable)e.getSource();

//               selectedRowNdx = tableSelection.getSelectedRow();          
//               selectedColNdx = tableSelection.getSelectedColumn();     

                 selectedDHParamtersTableCol = dhParametersTable.getSelectedColumn();

               selectedRowNdx = dhParametersTable.getSelectedRow();          
               selectedColNdx = dhParametersTable.getSelectedColumn();     
 
//               dhRowsSelected = dhParametersTable.getSelectedRows();

               if (((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)) 
               {

//                    System.out.println("selectedColNdx = " + selectedColNdx);
//                    System.out.println("selectedRowNdx = " + selectedRowNdx);

               }

               glcanvas.repaint();
 
               if (((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)) 
               {
//                    if (selectedColNdx == 0)
                    if (selectedDHParamtersTableCol == 0); 
                   {
                         System.out.println("GOOFY");

                         RMBclicked = true;
                    }
               }

/*
//               if (selectedColNdx == 0)
               if (selectedColNdx == 1)
               {
//                    if (nrowsSelected.length > 0)
//                    {
                         strSelectedDihedrals = (dhParametersTable.getValueAt(selectedRowNdx, 1)).toString();

                         if (!rowndxList.contains(selectedRowNdx))
                         {
                              rowndxList.add(selectedRowNdx);
                              selectedDihedrals.add(strSelectedDihedrals);
                         }
*/


/*
                         if (rowndxList.contains(selectedRowNdx))
                         {
                              alIndex = rowndxList.indexOf(selectedRowNdx);

                             rowndxList.remove(alIndex);
                             selectedDihedrals.remove(alIndex);
                         }
                         else
                         {
                              rowndxList.add(selectedRowNdx);
                              selectedDihedrals.add(strSelectedDihedrals);
                         }
                    }
               }
*/

/*
               else if (selectedColNdx == 0)                
               {
                    if (nrowsSelected.length > 0)
                    {
                         if (rowndxList.contains(selectedRowNdx))
                         {
                              alIndex = rowndxList.indexOf(selectedRowNdx);

                              rowndxList.remove(alIndex);
                              selectedDihedrals.remove(alIndex);
                         }
                    }

               }

          System.out.println();
          if (rowndxList.size() > 0)
          {
               System.out.println("rowndxList elements:");

               for (k = 0; k < rowndxList.size(); k++)
               {
                    System.out.print(rowndxList.get(k) + " ");
               }
           
               System.out.println();
               
               dhParametersTable.repaint();
          }

*/
               glcanvas.repaint();
          }
     }

     class dhListSelectionListener extends MouseAdapter implements ListSelectionListener 
     {
          int rowSelected, colSelected;
          int selectedRowNdx, deletedNdx;
          int ntableRows;
          int ndxx1, ndxx2, maxndxx, minndxx;
     
          int[] dhRowsSelected;

          ArrayList<Integer> indexxes;
          ArrayList<String> strDHCentralBondIndices;

          Object strSelection;

          String strng, regexstrng, strCBIndices;
          Pattern pattern;
          Matcher matcher;

          public void valueChanged(ListSelectionEvent e) 
          {
               ListSelectionModel lsm = (ListSelectionModel)e.getSource();

               if (mouseTblClicks == 2)    
               {
                    mouseTblClicks = 0;
               }

               mouseTblClicks++;

//               dhRowsSelected = dhParametersTable.getSelectedRows();

               ntableRows = dhParametersTable.getRowCount();

               if (mouseTblClicks == 1)
               {
                    indexxes = new ArrayList<Integer>();
                    strDHCentralBondIndices = new ArrayList<String>();

                    dhCentralBondIndices.clear();
                    minDHCentralBondIndices.clear();
                    maxDHCentralBondIndices.clear();

                    strDHCentralBondIndices.clear();

//                    rowSelected = dhParametersTable.getSelectedRow();
                    colSelected = dhParametersTable.getSelectedColumn();

                    dhRowsSelected = dhParametersTable.getSelectedRows();

                    nrowsSelected = dhRowsSelected.length;

ArrayList<String> dhAtomTypeList = new ArrayList<String>();

                    for (j = 0; j < dhRowsSelected.length; j++)
                    {
                         strSelection = (dhParametersTable.getValueAt(dhRowsSelected[j], 1));
                         strng = strSelection.toString();
                         strng = strng.trim();
 
                         pattern = Pattern.compile("\\s+");
                         matcher = pattern.matcher(strng);
                         regexstrng = matcher.replaceAll(" ");
 
dhAtomTypeList.add(regexstrng);

                         for (k = 0; k < arrDHAtomTypes.length; k++)
                         {
                              if ((arrDHAtomTypes[k].trim()).equals(regexstrng))
                              {
                                   indexxes.add(k);
                              }
                         }
                    }

strDHAtmTypTxtArea = new String();

for (k = 0; k < dhAtomTypeList.size(); k++)
{
    strDHAtmTypTxtArea += dhAtomTypeList.get(k) + '\n';
}

setDHAtmTypTxtArea(dhAtomTypeList);


                    for (k = 0; k < indexxes.size(); k++)
                    {
                         ndxx1 = alDihedralAtoms.get((4*indexxes.get(k)) + 1);
                         ndxx2 = alDihedralAtoms.get((4*indexxes.get(k)) + 2);

                         minndxx = Math.min(ndxx1, ndxx2);
                         maxndxx = Math.max(ndxx1, ndxx2);
                    
                         strCBIndices = Integer.toString(minndxx) + "-" + Integer.toString(maxndxx);


                         if (!strDHCentralBondIndices.contains(strCBIndices))
                         {
                              strDHCentralBondIndices.add(strCBIndices);
   
                              minDHCentralBondIndices.add(minndxx);  maxDHCentralBondIndices.add(maxndxx);
                         }
                    }



//selectedRow = dhParametersTable.getSelectedRow();
//dhToolTipText = "Row " + selectedRow + " has been selected";


//               tableCellRenderer.setToolTipText(getDhToolTip());

//               tableCellRenderer.setToolTipText(dhToolTipText);


                    if (nrowsSelected > 0)
                    {
                         selectedRowNdx = dhRowsSelected[nrowsSelected - 1];



System.out.println("selectedRowNdx = " + selectedRowNdx);



//   strSelection = dhParametersTable.getValueAt(rowSelected, colSelected);
//   strSelection = dhParametersTable.getValueAt(selectedRowNdx, colSelected);

                    }

//   System.out.println("colSelected = " + colSelected);

/*
                    if (colSelected == 0)
                    {
                         if (nrowsSelected > 0)
                         {
                              if (selectedRowNdxList.contains(selectedRowNdx))
                              {
                                   deletedNdx = selectedRowNdxList.indexOf(selectedRowNdx);
     
                                   selectedRowNdxList.remove(deletedNdx);
                              }
                         }
                    } 
                    else if (colSelected == 1)
                    {
                         if (!selectedRowNdxList.contains(selectedRowNdx))
                         {
                             selectedRowNdxList.add(selectedRowNdx);
                         }
                    }
*/


                    glcanvas.repaint();
               }
          }
     }

     public static void main (String[] args)
     {
          final Nanocad3D app = new Nanocad3D();

          app.setVisible(true);
          glcanvas.requestFocusInWindow();

JOptionPane.showMessageDialog(glcanvas, "           This version of Paramcad is for alpha testing only" + '\n' + '\n' + "CGenFF has been seleted as the default force field for this version.");

     }
}
