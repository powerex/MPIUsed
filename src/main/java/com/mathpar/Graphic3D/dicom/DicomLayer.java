/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.dicom;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author yuri
 */
public class DicomLayer {

    Dcm2Txt dcm2txt = new Dcm2Txt();
    Dcm2Jpg dcm2jpg = new Dcm2Jpg();
    public int layersCount;
    int currentLayer;
    double sliceLocation;
    private String path = "/home/yuri/";
//    private String fileNames[] = {"dcm/2-01.dcm", "dcm/2-02.dcm", "dcm/2-03.dcm"};
//    private String fileNames[] = {"dcm/2-04.dcm", "dcm/2-05.dcm", "dcm/2-06.dcm"};
//    private String fileNames[] = {"dcm/2-07.dcm", "dcm/2-08.dcm", "dcm/2-09.dcm", "dcm/2-10.dcm", "dcm/2-11.dcm", "dcm/2-12.dcm", "dcm/2-13.dcm", "dcm/2-14.dcm", "dcm/2-15.dcm", "dcm/2-16.dcm", "dcm/2-17.dcm", "dcm/2-18.dcm", "dcm/2-19.dcm", "dcm/2-20.dcm", "dcm/2-21.dcm", "dcm/2-22.dcm", "dcm/2-23.dcm", "dcm/2-24.dcm"};
    private String fileNames[] = {"dcm/2-25.dcm", "dcm/2-26.dcm", "dcm/2-27.dcm", "dcm/2-28.dcm", "dcm/2-29.dcm", "dcm/2-30.dcm", "dcm/2-31.dcm", "dcm/2-32.dcm", "dcm/2-33.dcm", "dcm/2-34.dcm", "dcm/2-35.dcm", "dcm/2-36.dcm", "dcm/2-37.dcm", "dcm/2-38.dcm", "dcm/2-39.dcm", "dcm/2-40.dcm"};
//    private String fileNames[] = {"dcm/2-41.dcm", "dcm/2-42.dcm", "dcm/2-43.dcm", "dcm/2-44.dcm", "dcm/2-45.dcm", "dcm/2-46.dcm", "dcm/2-47.dcm", "dcm/2-48.dcm", "dcm/2-49.dcm", "dcm/2-50.dcm", "dcm/2-51.dcm", "dcm/2-52.dcm", "dcm/2-53.dcm", "dcm/2-54.dcm", "dcm/2-55.dcm", "dcm/2-56.dcm", "dcm/2-57.dcm", "dcm/2-58.dcm"};
    public double array3D[][][];

    public DicomLayer() {

        String buf[] = new String[20];
        int i = 0;
        for (String curFName : fileNames) {
            File ifile = new File(path+curFName);
            try {
                dcm2txt.dump(ifile);
                dcm2jpg.convert2array(ifile);
                if (array3D == null) {
                    array3D = new double[dcm2jpg.bi.getWidth()][dcm2jpg.bi.getHeight()][fileNames.length];
                }
                for (int x = 0; x < dcm2jpg.bi.getWidth(); x++) {
                    for (int y = 0; y < dcm2jpg.bi.getHeight(); y++) {
                        array3D[x][y][i] = (dcm2jpg.bi.getRGB(x, y)) & 0xFF;
                    }
                }
//                buf[i++] = dcm2txt.tag2Val.get(new Integer(2097171));
//                buf[i++] = dcm2txt.tag2Val.get(new Integer(2101313));
                i++;
            } catch (IOException e) {
                System.err.println("dcm2txt: Failed to dump " + ifile + ": " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }

        for (String s : buf) {
            System.out.println(s);
        }
    }
}
