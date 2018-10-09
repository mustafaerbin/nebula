package com.tr.nebula.common.exception;

/**
 * Created by Mustafa Erbin on 13/03/2017.
 */
public enum ErrorCode {
   HTTP_100("Continue"),
   HTTP_101("Switching Protocols"),
   HTTP_102("Processing"),
   HTTP_200("OK"),
   HTTP_201("Created"),
   HTTP_202("Accepted"),
   HTTP_203("Non-Authoritative Information"),
   HTTP_204("No Content"),
   HTTP_205("Reset Content"),
   HTTP_206("Partial Content"),
   HTTP_207("Multi-Status"),
   HTTP_210("Content Different"),
   HTTP_300("Multiple Choices"),
   HTTP_301("Moved Permanently"),
   HTTP_302("Moved Temporarily"),
   HTTP_303("See Other"),
   HTTP_304("Not Modified"),
   HTTP_307("Temporary Redirect"),
   HTTP_305("Use Proxy"),
   HTTP_400("Bad Request"),
   HTTP_401("Unauthorized"),
   HTTP_402("Payment Required"),
   HTTP_403("Forbidden"),
   HTTP_404("Not Found"),
   HTTP_405("Not allowed method"),
   HTTP_406("Not Acceptable"),
   HTTP_407("Unauthorized on proxy"),
   HTTP_408("Request timeout"),
   HTTP_409("Conflicts connections"),
   HTTP_410("Gone"),
   HTTP_411("Length Required"),
   HTTP_412("Precondition Failed"),
   HTTP_413("Request Entity Too Large"),
   HTTP_414("Request-URI Too Long"),
   HTTP_415("Unsupported Media Type"),
   HTTP_416("Requested range unsatifiable"),
   HTTP_417("Expectation failed"),
   HTTP_422("Unprocessable entity"),
   HTTP_423("Locked"),
   HTTP_424("Method failure"),
   HTTP_500("Internal Server Error"),
   HTTP_501("Not Implemented"),
   HTTP_502("Invalid gateway"),
   HTTP_503("Service unavaliable"),
   HTTP_504("Gateway Timeout"),
   HTTP_505("HTTP Version not supported"),
   HTTP_507("Insufficient storage"),
   HTTP_508("Wrong URL "),
   HTTP_520("Requested JSON parse failed."),
   HTTP_521("Time out error."),
   HTTP_523("Ajax request aborted.");

   private String message;

   ErrorCode(String message){
      this.message = message;
   }

   public String getMessage() {
      return message;
   }
}
