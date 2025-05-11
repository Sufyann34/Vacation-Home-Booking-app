from setuptools import setup

setup(
    name='pds-cli',
    version='1.0',
    py_modules=['pds_cli'],
    install_requires=[
        'requests',
        'tabulate',
    ],
    entry_points={
        'console_scripts': [
            'pds = pds_cli:main_loop',
        ],
    },
)
