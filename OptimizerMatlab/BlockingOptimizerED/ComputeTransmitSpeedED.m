function transmitSpeedED = ComputeTransmitSpeedED(compressionRatio, divisionPercentageED, computeCapacityED)

transmitSpeedED = ((compressionRatio - 1) * divisionPercentageED + 1) .* computeCapacityED ./ divisionPercentageED  ; 

end