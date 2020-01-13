package excel;

import java.io.File;
import javax.swing.filechooser.*;


public class ExcelFileFilter extends FileFilter {
    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        return getExtension(f).equals(".xlsx");
    }

    //The description of this filter
    public String getDescription() {
        return "Your previous album excel";
    }

    private String getExtension(File f) {
        String extension = "";

        int i = f.getPath().lastIndexOf('.');
        if (i > 0) {
            extension = f.getPath().substring(i+1);
        }
        return extension;
    }
}