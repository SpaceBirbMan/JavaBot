module com.cnstl.wend_mach.jxl4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires com.google.gson;
    requires org.json;
    requires org.jsoup;

    opens com.cnstl.wend_mach.jxl4 to javafx.fxml, com.google.gson;
    exports com.cnstl.wend_mach.jxl4;
}
