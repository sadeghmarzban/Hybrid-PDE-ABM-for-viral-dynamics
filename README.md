# Hybrid PDE-ABM model for viral dynamics

[![DOI](https://zenodo.org/badge/344106127.svg)](https://zenodo.org/badge/latestdoi/344106127)

This repository contains our implementation of the model communicated in the manuscript "*A hybrid PDE-ABM model for viral dynamics with application to SARS-CoV-2 and influenza*". Our java code is based on a free and open-source software package, HAL (Hybrid Automata Library) [[1]](https://journals.plos.org/ploscompbiol/article?id=10.1371/journal.pcbi.1007635).

We use a hybrid partial differential equation -- agent-based (PDE–ABM) model to describe the spatio-temporal viral dynamics in a cell population. The virus concentration is considered as a continuous variable and virus movement is modelled by diffusion, while changes in the states of epithelial cells (i.e. healthy, infected, dead) are represented by a stochastic agent-based model. The two subsystems are intertwined: the probability of an agent getting infected in the ABM depends on the local viral concentration, and the source term of viral production in the PDE is determined by the cells that are infected.

The repository also contains supplementary images to [2] -- they are dedicated to exploring the model’s sensitivity to different diffusion parameters and grasping the infection dynamics in more detail. The pictures were obtained by using diffusion values of 0.1, 0.2, ..., 0.9 σ^2 /min.

To run this model, you will need
1.) to download the latest version of [HAL](https://github.com/MathOnco/HAL.git),
2.) Java,
3.) and an editor (like [Intellij Idea](https://www.jetbrains.com/idea/download/#section=windows) or [Eclipse IDE](https://www.eclipse.org/downloads/packages/)).

This code is free to use as long as manuscript [2] is cited accordingly. The same holds for any potential further results obtained by the former.

## References: 
[[1]](https://journals.plos.org/ploscompbiol/article?id=10.1371/journal.pcbi.1007635) Bravo RR, Baratchart E, West J, Schenck RO, Miller AK, Gallaher J, et al. (2020) Hybrid Automata Library: A flexible platform for hybrid modeling with real-time visualization. PLoS Comput Biol 16(3): e1007635.

[2] Sadegh Marzban, Renji Han, Nóra Juhász, Gergely Röst, A hybrid PDE-ABM model for viral dynamics with application to SARS-CoV-2 and influenza, bioRxiv: https://doi.org/10.1101/2021.05.07.443053, 2021.

