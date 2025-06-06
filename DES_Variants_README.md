# DES Variants Implementation Guide

## Overview
This project implements four variants of the DES algorithm, each progressively simpler to demonstrate the impact of different components on the avalanche effect.

## DES Variants

### DES0 - Full DES Implementation
- **Key scheduling**: Full PC1 and PC2 transformations with left shifts
- **F-function**: Expansion → XOR with round key → S-boxes → P-box permutation
- **Description**: Complete standard DES algorithm implementation
- **Code reuse**: Base implementation with all DES components

### DES1 - No Key Scheduling
- **Key scheduling**: Skipped (no PC1/PC2 transformations)
- **F-function**: Expansion → S-boxes → P-box permutation
- **Description**: Simplified version that bypasses key scheduling
- **Code reuse**: Copies DES0 structure but removes key XOR operations

### DES2 - No S-boxes
- **Key scheduling**: Skipped (no PC1/PC2 transformations)
- **F-function**: Expansion → P-box permutation (with truncation to 32 bits)
- **Description**: Further simplified version that skips S-box substitution
- **Code reuse**: Based on DES1 but removes S-box operations
- **Note**: Truncates 48-bit expansion output to 32 bits for P-box input

### DES3 - Minimal F-function
- **Key scheduling**: Skipped (no PC1/PC2 transformations)
- **F-function**: Expansion → truncation to 32 bits
- **Description**: Most simplified version with minimal f-function
- **Code reuse**: Based on DES1 but removes both S-boxes and P-box
- **Note**: Only performs expansion and truncation

## Code Reuse Strategy

### Shared Components (100% reused)
- All permutation tables (INITIAL_PERMUTATION, FINAL_PERMUTATION, etc.)
- All S-box tables (S1_BOX through S8_BOX)
- Utility methods: `permutation()`, `expandRight()`, `functionXOR()`, `compareRound()`
- Overall Feistel structure with 16 rounds
- Constructor pattern and getter methods
- Avalanche effect analysis framework

### Variant-Specific Modifications
Each variant only modifies the f-function implementation inside the 16-round loop while keeping all other code identical.

## Integration with Main Class
The Main class instantiates all four variants and runs them simultaneously for comparative avalanche effect analysis, producing output tables showing bit differences across rounds.

## Compilation and Execution
```bash
javac *.java
java Main input.txt
```

## Bug Fix Applied
Fixed the `endPermutation()` method which was incorrectly concatenating instead of properly applying the permutation table.

## Summary

You now have a complete implementation with maximum code reuse:

**What you reused:**
- All DES constants and lookup tables (100% reuse)
- All utility methods (100% reuse) 
- Overall program structure (100% reuse)
- Bug fix applied to all variants

**What you customized:**
- Only the f-function implementation in each DES variant
- Class names and constructors
- Integration in Main.java for comparative analysis

This approach minimizes code duplication while allowing you to study the progressive impact of removing DES components on the avalanche effect.
