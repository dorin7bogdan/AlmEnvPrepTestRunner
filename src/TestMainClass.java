import com.opentext.automation.ado.MainClass;

public class TestMainClass {
    public static void main(String[] args) {
        String[] arr = new String[18];
        arr[0] = "http://10.168.86.112:8080/qcbin";
        arr[1] = "sa";
        arr[2] = "pass:" ;
        arr[3] = "DEFAULT";
        arr[4] = "CI_Integration" ;
        arr[5] = "1004" ;
        arr[6] = "newconf" ;
        arr[7] = "newnamed:DEV2" ;
        arr[8] = "assign:" ;
        arr[9] = "useasexisting:" ;
        arr[10] = "jsonpath:" ;
        arr[11] = "false" ;
        arr[12] = "partype1:Manual" ;
        arr[13] = "parname1:Parameters/MyParam" ;
        arr[14] = "parval1:77" ;
        arr[15] = "partype2:Manual";
        arr[16] = "parname2:Parameters/MyParam2";
        arr[17] = "parval2:ADO";
        MainClass.main(arr);
    }
}