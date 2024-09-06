import React, { Component } from 'react';
import { Button, Container, ButtonGroup, Table } from 'reactstrap';
//import { Link } from 'react-router-dom';


class AnimalsListReact extends Component {
    constructor(props) {
        super(props);
        this.state = {animals: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        const {loading, error, data} = useQuery(ACCOUNT_INFORMATION_QUERY);
        data.animals.
        fetch('/animals')
            .then(response => response.json())
            .then(data => this.setState({animals: data}));
    }

    async remove(id) {
        await fetch(`/animals/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedAnimals = [...this.state.animals].filter(i => i.id !== id);
            this.setState({animals: updatedAnimals});
        });
    }


    render() {
        const {animals, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const animalsList = animals.map(animal => {
            return <tr key={animal.name}>
                <td style={{whiteSpace: 'nowrap'}}>{animal.name}</td>
                <td>{animal.species}</td>
                <td>
                    {/*<ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/animals/" + animal.name}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(animal.name)}>Delete</Button>
                    </ButtonGroup>*/}
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <div>
                        {/*  <Button color="success" tag={Link} to="/animals/new">Add Animal</Button>*/}
                    </div>
                    <h3>Animals</h3>
                    <Table >
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Species</th>
                        </tr>
                        </thead>
                        <tbody>
                        {animalsList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default AnimalsListReact;
