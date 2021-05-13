# Hybrid PDE-ABM model for viral dynamics

In order to model in-host viral dynamics, we have developed a hybrid PDE-Agent-Based-Model (PDE-ABM) spatiotemporal system, where virus concentration is modeled as a continuous variable. At the same time, for the case of epithelial cells, we take a discrete spatial approach. 
This repository contains our implementation of this model in the manuscript "*A hybrid PDE-ABM model for viral dynamics with application to SARS-CoV-2 and influenza*", based on a free and open-source software package, HAL (Hybrid Automata Library) [[1]](https://journals.plos.org/ploscompbiol/article?id=10.1371/journal.pcbi.1007635). In order to run this model, you will need to download the latest version of [HAL](https://github.com/MathOnco/HAL.git), Java, and an editor (like [Intellij Idea](https://www.jetbrains.com/idea/download/#section=windows) or [Eclipse IDE](https://www.eclipse.org/downloads/packages/)). Using the code of this repository or using a result of a computation done by this code, manuscript [2] should be cited.

## References: 
[[1]](https://journals.plos.org/ploscompbiol/article?id=10.1371/journal.pcbi.1007635) Bravo RR, Baratchart E, West J, Schenck RO, Miller AK, Gallaher J, et al. (2020) Hybrid Automata Library: A flexible platform for hybrid modeling with real-time visualization. PLoS Comput Biol 16(3): e1007635.

[2] Sadegh Marzban, Renji Han, Nóra Juhász, Gergely Röst, A hybrid PDE-ABM model for viral dynamics with application to SARS-CoV-2 and influenza, bioRxiv: https://doi.org/10.1101/2021.05.07.443053, 2021.

