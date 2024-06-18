import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Alert, Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';

class CoffeeShopEdit extends Component {

  emptyItem = {
    name: '',
    address: '',
    phone: '',
    priceOfCoffee: '',
    powerAccessible: '',
    internetReliability: ''
  };

  constructor(props) {
    super(props);
    this.state = {
      item: this.emptyItem,
      errorMessage: null,
      isCreate: false
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  async componentDidMount() {
    this.state.isCreate = this.props.match.params.id === 'new'; // are we editing or creating?
    if (!this.state.isCreate) {
      const response = await this.props.api.getById(this.props.match.params.id);
      const coffeeShop = await response.json();
      this.setState({item: coffeeShop});
    }
  }

  handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    let item = {...this.state.item};
    item[name] = value;
    this.setState({item});
  }

  async handleSubmit(event) {
    event.preventDefault();
    const {item, isCreate} = this.state;

    let result = isCreate ? await this.props.api.create(item) : await this.props.api.update(item);

    if (!result.ok) {
      this.setState({errorMessage: `Failed to ${isCreate ? 'create' : 'update'} record: ${result.status} ${result.statusText}`})
    } else {
      this.setState({errorMessage: null});
      this.props.history.push('/coffee-shops');
    }

  }

  render() {
    const {item, errorMessage, isCreate} = this.state;
    const title = <h2>{isCreate ? 'Add Coffee Shop' : 'Edit Coffee Shop'}</h2>;

    return (
      <div>
        {this.props.navbar}
        <Container style={{textAlign: 'left'}}>
          {title}
          {errorMessage ?
            <Alert color="warning">
              {errorMessage}
            </Alert> : null
          }
          <Form onSubmit={this.handleSubmit}>
            <div className="row">
              <FormGroup className="col-md-8 mb-3">
                <Label for="name">Name</Label>
                <Input type="text" name="name" id="name" value={item.name || ''}
                       onChange={this.handleChange} autoComplete="name"/>
              </FormGroup>
              <FormGroup className="col-md-4 mb-3">
                <Label for="phone">Phone</Label>
                <Input type="text" name="phone" id="phone" value={item.phone || ''}
                       onChange={this.handleChange} autoComplete="phone"/>
              </FormGroup>
            </div>
            <FormGroup>
              <Label for="address">Address</Label>
              <Input type="text" name="address" id="address" value={item.address || ''}
                     onChange={this.handleChange} autoComplete="address-level1"/>
            </FormGroup>
            <div className="row">
              <FormGroup className="col-md-4 mb-3">
                <Label for="priceOfCoffee">Price of Coffee</Label>
                <Input type="text" name="priceOfCoffee" id="priceOfCoffee" value={item.priceOfCoffee || ''}
                       onChange={this.handleChange}/>
              </FormGroup>
              <FormGroup className="col-md-4 mb-3">
                <Label for="powerAccessible">Power Accessible?</Label>
                <Input type="select" name="powerAccessible" id="powerAccessible"
                       value={item.powerAccessible === 'true' ? 'true' : 'false'}
                       onChange={this.handleChange}>
                  <option value="true">Yes</option>
                  <option value="false">No</option>
                </Input>
              </FormGroup>
              <FormGroup className="col-md-4 mb-3">
                <Label for="internetReliability">Internet Reliability</Label>
                <Input type="select" name="internetReliability" id="internetReliability"
                       value={item.internetReliability || '-'}
                       onChange={this.handleChange}>
                  <option>1</option>
                  <option>2</option>
                  <option>3</option>
                  <option>4</option>
                  <option>5</option>
                  <option value="-">-</option>
                </Input>
              </FormGroup>
            </div>
            <FormGroup>
              <Button color="primary" type="submit">Save</Button>{' '}
              <Button color="secondary" tag={Link} to="/coffee-shops">Cancel</Button>
            </FormGroup>
          </Form>
        </Container>
      </div>
    );
  }
}

export default withRouter(CoffeeShopEdit);