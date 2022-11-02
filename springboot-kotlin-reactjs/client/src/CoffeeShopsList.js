import React, { Component } from 'react';
import {
  Alert,
  Button
} from 'reactstrap';
import { Link } from 'react-router-dom';

const CoffeeShop = (props) => (
  <div className="coffeeshop-container p-2 m-2 d-flex flex-column">
    <h3>{props.name}</h3>
    <div className="coffeeshop-body">
      <div className="subtitle-container">
        <div>Cost: ${props.priceOfCoffee} / cup</div>
        <div>Internet Reliability: {props.internetReliability} / 5 </div>
        <div>{props.powerAccessible ? 'Power Accessible' : 'Power NOT Accessible'} </div>
      </div>
      <div>{props.address}</div>
      <div>{props.phone}</div>
    </div>
    <div className="coffeeshop-footer">
      <Button color="secondary" tag={Link} to={'/coffee-shops/' + props.id}>Edit</Button>
      <Button color="danger" onClick={() => props.remove(props.id)}>Delete</Button>
    </div>
  </div>
);

class CoffeeShopsList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      coffeeShops: [],
      isLoading: true,
      errorMessage: null
    };
    this.remove = this.remove.bind(this);
  }

  async componentDidMount() {
    this.setState({isLoading: true});
    const response = await this.props.api.getAll();
    if (!response.ok) {
      this.setState({
          errorMessage: `Failed to load coffee shops: ${response.status} ${response.statusText}`,
          isLoading: false
        }
      )
    }
    else {
      const body = await response.json();
      const coffeeShops = body._embedded.coffeeshops;
      this.setState({
        coffeeShops: coffeeShops,
        isLoading: false,
        errorMessage: null
      });
    }
  }

  async remove(id) {
    let response = await this.props.api.delete(id);
    if (!response.ok) {
      this.setState({errorMessage: `Failed to delete coffee shop: ${response.status} ${response.statusText}`})
    }
    else {
      let updatedCoffeeShops = [...this.state.coffeeShops].filter(i => i.id !== id);
      this.setState({coffeeShops: updatedCoffeeShops, errorMessage: null});
    }
  }

  render() {
    const {coffeeShops, isLoading, errorMessage} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    return (
      <div>
        {this.props.navbar}
        <div className="d-flex flex-row justify-content-between p-3">
          <h3 className="coffee-shops-title">Coffee Shops</h3>
          <Button color="success" tag={Link} to="/coffee-shops/new">Add New</Button>
        </div>
        {errorMessage ?
          <div className="d-flex flex-row justify-content-center">
            <Alert color="warning" style={{flex:1, maxWidth:'80%'}}>
              {errorMessage}
            </Alert>
          </div> : null
        }
        <div className="d-flex flex-row flex-container flex-wrap justify-content-center">
          {coffeeShops.map( coffeeShop =>
            <CoffeeShop {...coffeeShop} remove={this.remove.bind(this)} key={coffeeShop.id}/>
          )}
          {!coffeeShops || coffeeShops.length === 0 ? <p>No coffee shops!</p> : null}
        </div>
      </div>
    );
  }
}

export default CoffeeShopsList;