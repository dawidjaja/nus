import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;

import static org.testng.Assert.*;

public class QuestionTreeTest {
    private ArrayList<QuestionObject> readObjectFile(String objectFileName){
        try{
            // Open the file
            ArrayList<QuestionObject> objArray = null;
            FileReader f = new FileReader(objectFileName);
            BufferedReader buff = new BufferedReader(f);

            // Read the first line of the file and
            // parse the number of objects
            String line = buff.readLine().trim();
            int objCount = Integer.parseInt(line);
            objArray = new ArrayList<QuestionObject>(objCount);

            // Read in each object
            for (int i=0; i<objCount; i++){
                // The first line of the object contains its name
                String name = buff.readLine().trim();
                // The second line of the object contains the number of properties
                String propCountLine = buff.readLine().trim();
                int propCount = Integer.parseInt(propCountLine);

                // Now we loop and read in each property
                TreeSet<String> props = new TreeSet<String>();
                TreeSet<String> props2 = new TreeSet<String>();
                for (int j=0; j<propCount; j++){
                    String propName = buff.readLine().trim();
                    if ((propName != null) && (propName != "")) {
                        props.add(propName);
                        props2.add(propName);
                    }
                }

                // Once we have all the properties, create a new QuestionObject
                if ((name != null) && (name != "") && (props.size() > 0)){
                    QuestionObject qObj = new QuestionObject(name, props);
                    objArray.add(qObj);
                    htQObject.put(name, new QuestionObject(name, props2));
                }
            }
            f.close();
            buff.close();
            return objArray;
        }
        catch(Exception e)
        {
            // If there is an error reading in the file, there isn't much we can do.
            // Print out an error, and later the program will exit.
            System.out.println(e);
            System.out.println("Unable to read in object database.  Please check the filename, the path, and that the file is formatted correctly.");
        }
        return null;
    }

    private Hashtable<String, QuestionObject> htQObject = new Hashtable<String, QuestionObject>();

    private void testBuildTree(String testDBName){
        ArrayList<QuestionObject> qos = readObjectFile(testDBName);
        QuestionTree qt = new QuestionTree();
        qt.buildTree(qos);

        PrintStream console = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        qt.printTree();
        System.setOut(console);

        String tree = baos.toString();
        String[] leafs = tree.split("(" + System.getProperty("line.separator") + ")|([\\n\\r]+)");

        // Test tree properties
        String[] arr;
        for(String s : leafs){
            arr = s.split(":");
            String objName = arr[0].trim();
            String objProp = arr[1].trim();

            QuestionObject qObj = htQObject.get(objName);
            assertEquals(objName, qObj.getName());
            String[] properties = objProp.split("[\\s]+");
            int countProps = 0;
            for(String p : properties){
                if(p.charAt(0) == '-')
                    assertTrue(!qObj.containsProperty(p.substring(1)));
                else {
                    assertTrue(qObj.containsProperty(p));
                    ++countProps;
                }
            }
            assertEquals(countProps, qObj.getPropCount());
        }
    }

    @Test
    public void testBuildTree0() throws Exception {
        System.out.println("------ Build tree: Test 00 ------");
        testBuildTree("TestDB_00.txt");
    }

    @Test
    public void testBuildTree1() throws Exception {
        System.out.println("------ Build tree: Test 01 ------");
        testBuildTree("TestDB_01.txt");
    }

    @Test
    public void testBuildTree2() throws Exception {
        System.out.println("------ Build tree: Test 02 ------");
        testBuildTree("TestDB_02.txt");
    }

    @Test
    public void testBuildTree3() throws Exception {
        System.out.println("------ Build tree: Test 03 ------");
        testBuildTree("TestDB_03.txt");
    }

    @Test
    public void testBuildTree4() throws Exception {
        System.out.println("------ Build tree: Test 04 ------");
        testBuildTree("TestDB_04.txt");
    }


    private void testFindQuery(String testDBName){
        ArrayList<QuestionObject> qos = readObjectFile(testDBName);
        QuestionTree qt = new QuestionTree();
        qt.buildTree(qos);

        Query q = qt.findQuery();

        TreeNode<String> v = qt.search(q);

        int countV = qt.countObjects(v);
        int n = qt.countObjects();

        assertTrue(n/3 < countV && countV <= 2*n/3);
    }


    @Test
    public void testFindQuery0() throws Exception {
        System.out.println("------ Find Query: Test 00 ------");
        testFindQuery("TestDB_00.txt");
    }

    @Test
    public void testFindQuery1() throws Exception {
        System.out.println("------ Find Query: Test 01 ------");
        testFindQuery("TestDB_01.txt");
    }

    @Test
    public void testFindQuery2() throws Exception {
        System.out.println("------ Find Query: Test 02 ------");
        testFindQuery("TestDB_02.txt");
    }

    @Test
    public void testFindQuery3() throws Exception {
        System.out.println("------ Find Query: Test 03 ------");
        testFindQuery("TestDB_03.txt");
    }

    @Test
    public void testFindQuery4() throws Exception {
        System.out.println("------ Find Query: Test 04 ------");
        testFindQuery("TestDB_04.txt");
    }
}
