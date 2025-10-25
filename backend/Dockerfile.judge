FROM openjdk:17-jdk-slim

# Install Python and C++ compiler
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    g++ \
    && rm -rf /var/lib/apt/lists/*

# Create workspace directory
WORKDIR /workspace

# Set up Python
RUN ln -s /usr/bin/python3 /usr/bin/python

# Default command
CMD ["bash"]
