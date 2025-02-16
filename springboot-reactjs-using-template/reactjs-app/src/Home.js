import React, { Component } from 'react';
import './App.css';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';

class Home extends Component {

  render() {
    return (
      <div className="app">
        {this.props.navbar}
        <Container fluid>
          <div>
            <Button color="secondary">
              <Link className="app-link" to="/coffee-shops">Manage Coffee Shops</Link>
            </Button>
          </div>
        </Container>
      </div>
    );
  }
}

export default Home;