# Diffusion Simulation
## Introduction
College project simulating gas diffusion using Lattice Boltzmann methods (LBM) and cellular automata.

## Step by step
In order to run the simulation, the following operations are performed continuously one after the other:
* Streaming operation
* Calculations of macroscopic values
* Equilibrium distribution calculations
* Collision operation
* Boundary conditions (bounce-back)

The initial conditions of the simulation consist in assigning a specific concentration (in this case equal to one) to a specific area. 
The rest of the computational area may be zero. 

Additionally, the gas with the specific concentration and walls with definite boundary conditions can be added with the mouse input.

### The final result
<p align="center">
    <img src="./output/gas_diffusion_gif_500x400.gif">
</p>

## Sources and inspirations
* https://en.wikipedia.org/wiki/Lattice_Boltzmann_methods
* https://en.wikipedia.org/wiki/Cellular_automaton