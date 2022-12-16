//MOHAMMED ABD EL-KAREEM (20102181)
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Project extends Variables
{
    public static void main (String[] args)
    {
        /*-----------------Throw Exception-------------------*/ 
        try
        {
            /*-------------Scan File Path--------------------*/ 
            File sicxe = new File("inSICXE.txt");
            Scanner scanTxt = new Scanner(sicxe);
            k = 0;
            
            /*-----------------Read The Data-----------------*/
            while (scanTxt.hasNextLine())
            {
                String data = scanTxt.nextLine();
                i = 0;

                for (j = 0 ; j<data.length() ;)
                {
                    String arr[] = data.split(" ");
                    i = 0;
                    
                    /*--------------If The Line Has Reference------------------------*/
                    if (arr.length == 3)
                    {
                        //System.out.println("has Reference"); // For Debugging Testing
                        
                    while (i < arr.length)
                    {
                        Label[k] = arr[i];
                        //System.out.println(Label[k]); // For Debugging Testing
                        i++;
                        Inst[k] = arr[i];
                        //System.out.println(Inst[k]); // For Debugging Testing
                        i++;
                        Ref[k] = arr[i];
                        //System.out.println(Ref[k]); // For Debugging Testing
                        i++;
                        k++;
                    }

                    break;

                    }

                    /*--------------If The Line Does'nt have Reference------------------------*/
                    if(arr.length == 2)
                    {
                        //System.out.println("Doesn't have Reference"); // For Debugging Testing

                    while (i < arr.length)
                    {
                        Label[k] = " ";
                        //System.out.println(Label[k]); // For Debugging Testing
                        Inst[k] = arr[i];
                        //System.out.println(Inst[k]); // For Debugging Testing
                        i++;
                        Ref[k] = arr[i];
                        //System.out.println(Ref[k]); // For Debugging Testing
                        i++;
                        k++;
                    }
                    
                    break;

                    }

                    /*--------------If The Line Has Instruction Only------------------------*/
                    if(arr.length == 1)
                    {
                        //System.out.println("Doesn't have Reference"); // For Debugging Testing

                    while (i < arr.length)
                    {
                        Label[k] = " ";
                        //System.out.println(Label[k]); // For Debugging Testing
                        Inst[k] = arr[i];
                        //System.out.println(Inst[k]); // For Debugging Testing
                        i++;
                        Ref[k] = " ";
                        //System.out.println(Ref[k]); // For Debugging Testing
                        k++;
                    }
                    
                    break;

                    }

                    for (k = 0 ; k < arr.length ; k++)
                    {
                        //System.out.println(arr[k]); // For Debugging Testing
                    }

                    break;
                    
                }
                
                //System.out.println(data); // For Debugging Testing
            }

            scanTxt.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("CAN'T READ THE FILE");
        }

        /*------------------Get The Location Address----------------*/
        Locc [0] = Ref[0];
        Locc [1] = Locc[0];
        i = 1;
        dcOP = 0;

        for (j = 1 ; j<Inst.length-1 ; j++)
        {
            if (Inst[j].equals("BASE"))
            {
                Locc[++i] = Integer.toHexString(dcOP);
                //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
            }

            else if (Inst[j].equals("+" + converter.ReadInst(Inst[j].replace("+",""))))
            {
                dcOP += 4;
                Locc[++i] = Integer.toHexString(dcOP);
                //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
            }

            else if (Inst[j].equals("BYTE"))
            {
                pattern = Pattern.compile("C");
                matcher = pattern.matcher(Ref[j]);
                
                if (matcher.find())
                {
                    dcOP += (Ref[j].length()-3);
                    Locc[++i] = Integer.toHexString(dcOP);
                    //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
                }

                else
                {
                    dcOP += ((Ref[j].length()-3)/2);
                    Locc[++i] = Integer.toHexString(dcOP);
                    //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
                }
            }

            else if (Inst[j].equals("RESW"))
            {
                dcI = Integer.parseInt(Ref[j]);
                dcI *= 3;
                dcOP += dcI;
                Locc[++i] = Integer.toHexString(dcOP);
                //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
            }

            else if (Inst[j].equals("RESB"))
            {
                dcI = Integer.parseInt(Ref[j]);
                dcLocc = Integer.parseInt(Locc[i],16);
                dcOP = dcI + dcLocc;
                Locc[++i] = Integer.toHexString(dcOP);
                //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
            }

            else if (Inst[j].equals("WORD"))
            {
                dcOP +=3;
                Locc[++i] = Integer.toHexString(dcOP);
                //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
            }

            else if (!converter.ReadInst(Inst[j]).equals("NULL"))
            {
                format = converter.GetFormat(Inst[j]);

                dcI = Integer.parseInt(format,16);
                dcLocc = Integer.parseInt(Locc[i],16);
                dcOP = dcI + dcLocc;
                Locc[++i] = Integer.toHexString(dcOP);
                //System.out.println("The Location : " + Locc[i]); // For Debugging Testing
            }

            else
            {
                System.out.println("ERROR! CHECK YOUR FILE INPUTS");
                System.exit(0);
            }
            
        }
        
        /*------------------Get The Object Code-----------------------*/
        ObjectCode[0] = " ";

        for (j = 1 ; j<Inst.length ; j++)
        {
            if (Inst[j].equals("BASE") || Inst[j].equals("RESW") || Inst[j].equals("RESB") || Inst[j].equals("END"))
            {
                ObjectCode[j] = " ";
            }

            /*----------------Format 4------------------*/
            else if (Inst[j].equals("+" + converter.ReadInst(Inst[j].replace("+",""))))
            {
                B = "0";
                P = "0";
                E = "1";

                opcodeBi = OpCodeBinary(Inst[j].replace("+",""));

                // If Immediate And Next To It A Number
                patternHashNum = Pattern.compile("#[0-9]");
                matcherHashNum = patternHashNum.matcher(Ref[j]);

                //If Immediate And Next To It A Reference
                patternHashRef = Pattern.compile("#[A-Za-z]");
                matcherHashRef = patternHashRef.matcher(Ref[j]);

                //If Immediate Number
                if(matcherHashNum.find())
                {
                    N = "0";
                    I = "1";
                    X = "0";
                    B = "0";
                    P = "0";
                    E = "1";

                    num = Integer.parseInt(Ref[j].replace("#",""),10);
                    numBi = Integer.toBinaryString(num);

                    while (numBi.length() < 20)
                    {
                        numBi = zero + numBi;
                    }

                    finalCode = opcodeBi + N + I + X + B + P + E + numBi;
                    //System.out.println("Instruction : " + Inst[j]);
                    //System.out.println("code in Binary : " + finalCode); // For Debugging Testing
                    num = Integer.parseInt(finalCode,2);
                    ObjectCode[j] = Integer.toHexString(num);
                    //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing 
                }

                //If Immediate Reference
                else if (matcherHashRef.find())
                {
                    N = "0";
                    I = "1";
                    X = checkX(Ref[j]);
                    B = "0";
                    P = "0";
                    E = "1";

                    for (i = 0 ; i < Label.length ; i++)
                    {
                        if (Label[i].equals(Ref[j].replace("#","")))
                        {
                            num = Integer.parseInt(Locc[i],16);
                            numBi = Integer.toBinaryString(num);
                            break;
                        }
                    }

                    while (numBi.length() < 20)
                    {
                        numBi = zero + numBi;
                    }

                    finalCode = opcodeBi + N + I + X + B + P + E + numBi;
                    //System.out.println("Instruction : " + Inst[j]);
                    //System.out.println("code in Binary : " + finalCode); // For Debugging Testing
                    num = Integer.parseInt(finalCode,2);
                    ObjectCode[j] = Integer.toHexString(num);
                    //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing 
                }

                else
                {
                    N = "1";
                    I = "1";
                    X = checkX(Ref[j]);
                    B = "0";
                    P = "0";
                    E = "1";

                    for (i = 0 ; i < Label.length ; i++)
                    {
                        if (Label[i].equals(Ref[j]))
                        {
                            num = Integer.parseInt(Locc[i],16);
                            numBi = Integer.toBinaryString(num);
                            break;
                        }
                    }

                    while (numBi.length() < 20)
                    {
                        numBi = zero + numBi;
                    }

                    finalCode = opcodeBi + N + I + X + B + P + E + numBi;
                    //System.out.println("Instruction : " + Inst[j]);
                    //System.out.println("code in Binary : " + finalCode); // For Debugging Testing
                    num = Integer.parseInt(finalCode,2);
                    ObjectCode[j] = Integer.toHexString(num);
                    //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing 
                }
            }

            /*----------------Format 3------------------*/
            else if (converter.GetFormat(Inst[j]).equals("3"))
            {
                TA = 0; //Generic

                opcode = converter.GetOPCODE(Inst[j]);

                dcI = Integer.parseInt(opcode,16);
                opcodeBi = Integer.toBinaryString(dcI);
                if (opcodeBi.length()>2)
                opcodeBi = opcodeBi.substring(0,opcodeBi.length()-2);
                zero = '0';

                while (opcodeBi.length() < 6)
                {
                    opcodeBi = zero + opcodeBi;
                }

                // If Immediate And Next To It A Number
                patternHashNum = Pattern.compile("#[0-9]");
                matcherHashNum = patternHashNum.matcher(Ref[j]);

                //If Immediate And Next To It A Reference
                patternHashRef = Pattern.compile("#[A-Za-z]");
                matcherHashRef = patternHashRef.matcher(Ref[j]);

                //If Indirect
                patternIN = Pattern.compile("@[A-Za-z]");
                matcherIN = patternIN.matcher(Ref[j]);

                //If Immediate Number
                if (matcherHashNum.find())
                {
                    N = "0";
                    I = "1";
                    X = "0";
                    B = "0";
                    P = "0";
                    E = "0";

                    num = Integer.parseInt(Ref[j].replace("#",""),10);
                    numBi = Integer.toBinaryString(num);

                    while (numBi.length() < 12)
                    {
                        numBi = zero + numBi;
                    }

                    finalCode = opcodeBi + N + I + X + B + P + E + numBi;
                    //System.out.println("Instruction : " + Inst[j]);
                    //System.out.println("code in Binary : " + finalCode); // For Debugging Testing
                    num = Integer.parseInt(finalCode,2);
                    ObjectCode[j] = Integer.toHexString(num);
                    //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing 
                }

                //If Immediate Reference
                else if (matcherHashRef.find())
                {
                    N = "0";
                    I = "1";
                    X = checkX(Ref[j]);
                    E = "0";

                    GetObjectCode();
                }

                //If Indirect
                else if (matcherIN.find())
                {
                    N = "1";
                    I = "0";
                    X = checkX(Ref[j]);
                    E = "0";

                    GetObjectCode();
                }

                else
                {
                    N = "1";
                    I = "1";
                    X = checkX(Ref[j]);
                    E = "0";

                    GetObjectCode();  
                }
            }

            /*----------------Format 2------------------*/
            else if (converter.GetFormat(Inst[j]).equals("2"))
            {
                opcode = converter.GetOPCODE(Inst[j]);

                String s [] = Ref[i].split(",");
                
                for (i = 0 ; i < s.length ; i++)
                {
                    register [i] = s[i];
                }

                for (i = 0 ; i < s.length ; i++)
                {
                    if (Ref[j].equals("A"))
                    {
                        register[i] = "0";
                    }
                    if (Ref[j].equals("X"))
                    {
                        register[i] = "1";
                    }
                    if (Ref[j].equals("B"))
                    {
                        register[i] = "4";
                    }
                    if (Ref[j].equals("S"))
                    {
                        register[i] = "5";
                    }     
                    if (Ref[j].equals("T"))
                    {
                        register[i] = "6";
                    }  
                    if (Ref[j].equals("F"))
                    {
                        register[i] = "7";
                    }      
                }

                if (s.length == 2)
                {
                    ObjectCode[j] = opcode + register[0] + register[1];

                    //System.out.println("Instruction : " + Inst[j]); // For Debugging Testing
                    //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing
                }
                else
                {
                    ObjectCode[j] = opcode + register[0] + "0";

                    //System.out.println("Instruction : " + Inst[j]); // For Debugging Testing
                    //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing
                }
            }

            /*----------------Format 1------------------*/
            else if (converter.GetFormat(Inst[j]).equals("1"))
            {
                opcode = converter.GetOPCODE(Inst[j]);
                ObjectCode[j] = opcode;
            }

            /*----------------BYTE------------------*/
            else if (Inst[j].equals("BYTE"))
            {
                String ins = Ref[j];
                ins = ins.substring(1);
                ins = ins.replace("'", "");

                String letters = ins;
                int ascii;
                String code = "";
                for (i = 0 ; i < letters.length() ; i++)
                {
                    ascii = letters.charAt(i);
                    code = code + ascii;
                }

                while (code.length() < 6) 
                {
                    code = zero + code;
                }
                //System.out.println("Instruction : " + Inst[j]); // For Debugging Testing
                ObjectCode[j] = code;
                //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing
            }

            /*----------------WORD------------------*/
            else if (Inst[j].equals("WORD"))
            {
                String number = Ref[j];
                int numberINT = Integer.parseInt(number,10);
                number = Integer.toHexString(numberINT);

                while (number.length() < 6) 
                {
                    number = zero + number;
                }
                //System.out.println("Instruction : " + Inst[j]); // For Debugging Testing
                ObjectCode[j] = number;
                //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing
            }
        }
        /*------------------PRINT THE TABLE-----------------------*/
        System.out.println("////////////TABLE////////////////");
        System.out.println(" Lables  :: Instructions :: References :: Object Code");
        for (i = 0; i < Inst.length ; i++)
        {
            System.out.println(Label[i] + " :: " + Inst[i] + " :: " + Ref[i] + " :: " + ObjectCode[i]);
        }

        System.out.println("////////////HTE RECORD////////////////");

        /*------------------HTE-----------------------*/
        start = Locc[0];
        end = Locc[Locc.length-1];
        changeStartHex = Integer.parseInt(start,16);
        changeEndHex = Integer.parseInt(end,16);
        length = changeEndHex - changeStartHex;
        start = Integer.toHexString(changeStartHex);
        lengthStr = Integer.toHexString(length);

        while (start.length() < 6) 
        {
            start = zero + start;
        }

        while (lengthStr.length() < 6) 
        {
            lengthStr = zero + lengthStr;
        }
        System.out.println("H " + "^" + start + "^" + lengthStr);

        i = 1;
        k = 0;

        for (j = 1 ; j < Inst.length ; j++)
        {
            i = j;
            start = Locc[j];
            flagBase = false;
            while (k < 10)
            {
                if (Inst[i].equals("RESW") || Inst[i].equals("RESB") || Inst[i].equals("END"))
                {
                    break;
                }

                if (Inst[i].equals("BASE"))
                {
                    flagBase = true;
                }
                
                k++;
                i++;
            }

            if (i == 45)
            {
                k = i;
            }
            else
            {
                if (flagBase)
                {
                    i++;
                }
                k = i;
                flagBase = false;
            }
            
            end = Locc[k];
            changeStartHex = Integer.parseInt(start,16);
            changeEndHex = Integer.parseInt(end,16);
            length = changeEndHex - changeStartHex;
            start = Integer.toHexString(changeStartHex);
            lengthStr = Integer.toHexString(length);

            while (start.length() < 6) 
            {
                start = zero + start;
            }

            while (lengthStr.length() > 2) 
            {
                lengthStr = lengthStr.substring(1);
            }

            if (!ObjectCode[j].equals(" "))
            {
                System.out.print("T " + "^" + start + "^" +lengthStr + "^" + ObjectCode[j]);
                flag = false;
            }
            else
            {
                if (!ObjectCode[j+1].equals(" "))
                {
                    k = 0;
                    i++;
                    while (k < 10)
                    {
                        if (Inst[i].equals("RESW") || Inst[i].equals("RESB") || Inst[i].equals("END"))
                        {
                            break;
                        }
                        k++;
                        i++;
                    }
                    k = i;
                    end = Locc[k];
                    changeStartHex = Integer.parseInt(start,16);
                    changeEndHex = Integer.parseInt(end,16);
                    length = changeEndHex - changeStartHex;
                    start = Integer.toHexString(changeStartHex);
                    lengthStr = Integer.toHexString(length);

                    while (start.length() < 6) 
                    {
                        start = zero + start;
                    }

                    while (lengthStr.length() > 2) 
                    {
                        lengthStr = lengthStr.substring(1);
                    }

                    System.out.print("T " + "^" + start + "^" +lengthStr + "^" + ObjectCode[j+1]);
                    j++;
                    flag = false;
                }
                else
                {
                    flag = true;
                }
            }            

            for (i = 1 ; i < k ; i++)
            {
                if (Inst[j].equals("BASE"))
                {
                    k++;
                    flagBase = true;
                }

                j++;
                
                if (Inst[j].equals("RESW") || Inst[j].equals("RESB") || Inst[j].equals("END") || flag)
                {
                    k = 0;
                    System.out.println("\n");
                    break;
                }

                if (!ObjectCode[j].equals(" "))
                {
                    System.out.print(ObjectCode[j]);
                }

                if (flagBase)
                {
                    if (i == 10)
                    {
                        System.out.println("\n");
                        k = 0;
                    }
                }
                else
                {
                    if (i == 9)
                    {
                        System.out.println("\n");
                        k = 0;
                    }
                } 
            }
        }
        while (Locc[0].length() < 6) 
        {
            Locc[0] = zero + Locc[0];
        }
        System.out.println("E " + "^" + Locc[0]);
        
    }

    public static String OpCodeBinary (String convert)
    {
        String opcode = converter.GetOPCODE(convert);

        int dcI = Integer.parseInt(opcode,16);
        String opcodeBi = Integer.toBinaryString(dcI);
        if (opcodeBi.length()>2)
            opcodeBi = opcodeBi.substring(0,opcodeBi.length()-2);
        char zero = '0';

        while (opcodeBi.length() < 6)
        {
            opcodeBi = zero + opcodeBi;
        }

        return opcodeBi;
    }

    public static String checkX (String check)
    {
        String X;
        Pattern pattern = Pattern.compile(",X");
        Matcher matcher = pattern.matcher(check);

        if (matcher.find())
        {
            X = "1";
        }
        else
        {
            X = "0";
        }

        return X;
    }

    public static void GetObjectCode ()
    {
        for (i = 0; i < Label.length; i++) 
        {
            if (Label[i].equals(Ref[j]) || Label[i].equals(Ref[j].replace("@","")) || Label[i].equals(Ref[j].replace("#",""))) 
            {
                TA = Integer.parseInt(Locc[i], 16);
                break;
            }
        }

        if (!Ref[j].equals(" "))
        {
            pc = Integer.parseInt(Locc[j + 1], 16);
            dis = TA - pc;

            if (dis > -2048 && dis < 2047) 
            {
                P = "1";
                B = "0";
            } 
            else 
            {
                refLocation = "0"; // Generic Number
                for (i = 0; i < Inst.length; i++) 
                {
                    if (Inst[i].equals("LDB") || Inst[i].equals("+LDB")) 
                    {
                        refName = Ref[i].replace("#", "");
                        for (k = 0; k < Label.length; k++) 
                        {
                            if (refName.equals(Label[k])) 
                            {
                                refLocation = Locc[k];
                                break;
                            }
                        }
                        break;
                    }
                }

                base = Integer.parseInt(refLocation, 16);
                dis = TA - base;

                P = "0";
                B = "1";
            }

            disBi = Integer.toBinaryString(dis);
        }
        else
        {
            disBi = "";
        }

        if (disBi.length() < 12) 
        {
            while (disBi.length() < 12) 
            {
                disBi = zero + disBi;
            }
        } 
        else 
        {
            while (disBi.length() > 12) 
            {
                disBi = disBi.substring(1);
            }
        }

        finalCode = opcodeBi + N + I + X + B + P + E + disBi;
        System.out.println("Instruction : " + Inst[j]);
        //System.out.println("code in Binary : " + finalCode); // For Debugging Testing
        num = Integer.parseInt(finalCode, 2);
        ObjectCode[j] = Integer.toHexString(num);
        //System.out.println("Object Code : " + ObjectCode[j]); // For Debugging Testing
    }
    
}