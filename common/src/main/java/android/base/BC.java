package android.base;

public interface BC {
    int LENGTH_resRegNo = 13;
    /** 메일주소 인것 */
    String regularExpressionEmail = "^([\\w-\\.]+)@((?:[\\w]+\\.)+[a-zA-Z]{2,4})$";
    /** 전화번호 인것 */
    String regularExpressionPhoneNo = "^(0(?:505|70|10|11|16|17|18|19))(\\d{3}|\\d{4})(\\d{4})$";
    /** 파일네임에 적합하지 않은것 */
    String regularExpressionVFilename = "\\\\/|[&{}?=/\\\\: <>*|\\]\\[\\\"\\']";
    /** 자연수가 아닌것 */
    String regularExpressionNotdecimal = "\\D";
    /** Password로 사용가능한 문자열 */
    String regularExpressionPassword = "(((?=.*[a-z])|(?=.*[A-Z])).{8,20})";
    /** 엔터가 포함되어 있지 않은 ""로 둘러 쌓여있는 문자열 */
    String regularExpressionWrodNotCR = "(\\\"[^\\\"\\r]+\\\")";

    String KSC5601 = "KSC5601";
    String UTF8 = "UTF-8";

    String CRLF = "\r\n";

    int REQ_RECOGNIZER = 7580;
}
