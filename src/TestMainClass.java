import com.opentext.automation.ado.MainClass;

public class Main {
    public static void main(String[] args) {
        String[] arr = new String[19];
        arr[0] = "lep";
        arr[1] = "http://10.168.86.112:8080/qcbin";
        arr[2] = "sa";
        arr[3] = "pass:" ;
        arr[4] = "DEFAULT";
        arr[5] = "CI_Integration" ;
        arr[6] = "1004" ;
        arr[7] = "newconf" ;
        arr[8] = "newnamed:DEV2" ;
        arr[9] = "assign:" ;
        arr[10] = "useasexisting:" ;
        arr[11] = "jsonpath:" ;
        arr[12] = "false" ;
        arr[13] = "partype1:Manual" ;
        arr[14] = "parname1:Parameters/MyParam" ;
        arr[15] = "parval1:77" ;
        arr[16] = "partype2:Manual";
        arr[17] = "parname2:Parameters/MyParam2";
        arr[18] = "parval2:ADO";
        MainClass.main(arr);
    }
}