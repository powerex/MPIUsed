/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.ZBuffer;
/**
 * <p>Title:����� ������������ ��� �������� ����� � ���������� �� ��� �������� </p>
 *
 * <p>Description:�������� �����������,�������� �������� �����,����� �� ���������� ��������
 * �� �����,������ �� �������� �������� �� ����� � ������� �����  </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class GM_Scene extends GM_Figure{
    public GM_Light light;

    public int width;

    public int height;

    public int scale;

    public GM_Scene() {}

    public GM_Scene(GM_Light light, int width, int height, int scale) {
        this.light = light;
        this.width = width;
        this.height = height;
        this.scale = scale;
    }
}
