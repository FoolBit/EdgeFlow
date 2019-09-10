function transmitSpeed = ComputeTransmitSpeedED(compressionRatio, divisionPercentage, computeCapacity)

transmitSpeed = ((compressionRatio - 1) * divisionPercentage + 1) .* computeCapacity ./ divisionPercentage  ; 

end