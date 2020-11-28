/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.Graphic3D.RayTracing.Objects;

import com.mathpar.Graphic3D.RayTracing.Hit;
import com.mathpar.Graphic3D.RayTracing.Material;
import com.mathpar.Graphic3D.RayTracing.Ray;

/**
 *
 * @author yuri
 */
public abstract class AbstractObject {

    Material material;

    public abstract Hit getHit(Ray ray);
}
