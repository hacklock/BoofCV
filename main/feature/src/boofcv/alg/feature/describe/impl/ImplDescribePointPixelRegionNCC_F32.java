/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://www.boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.feature.describe.impl;

import boofcv.alg.feature.describe.DescribePointPixelRegionNCC;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.feature.NccFeature;
import boofcv.struct.image.ImageFloat32;

/**
 * Implementation of {@link boofcv.alg.feature.describe.DescribePointPixelRegionNCC}.
 *
 * @author Peter Abeles
 */
public class ImplDescribePointPixelRegionNCC_F32 extends DescribePointPixelRegionNCC<ImageFloat32> {

	public ImplDescribePointPixelRegionNCC_F32(int regionWidth, int regionHeight) {
		super(regionWidth, regionHeight);
	}

	@Override
	public boolean process(int c_x, int c_y, NccFeature desc) {

		// the entire region must be inside the image
		// because any outside pixels will change the statistics
		if(BoofMiscOps.checkInside(image,c_x,c_y,radiusWidth,radiusHeight)) {
			double mean = 0;
			int centerIndex = image.startIndex + c_y*image.stride + c_x;
			for( int i = 0; i < offset.length; i++ ) {
				mean += desc.value[i] = image.data[centerIndex + offset[i]];
			}
			mean /= offset.length;
			double variance = 0;
			for( int i = 0; i < desc.value.length; i++ ) {
				double d = desc.value[i] -= mean;
				variance += d*d;
			}
			variance /= offset.length;

			desc.mean = mean;
			desc.variance = variance;

			return true;
		}
		return false;
	}
}