import React, { Component } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import AddDish from './components/company/AddDish';
import EditDish from './components/company/EditDish';
import DeleteDish from './components/company/DeleteDish';
import ViewDatesOfDishes from './components/company/ViewDatesOfDishes';
import ListRestaurantToday from './components/user/ListRestaurantToday';
import Login from './components/authentication/Login';
import AuthenticatedRoute from './components/authentication/AuthenticatedRoute';
import SignUpUser from './components/signup/SignUpUser';
import MenuComponent from './components/MenuComponent';
import SignUpCompany from './components/signup/SignUpCompany';
import ViewDatesOfVotes from './components/statistic/ViewDatesOfVotes';
import ProfileRestaurant from './components/profile/ProfileRestaurant';
import RestaurantPage from './components/company/RestaurantPage';
import ProfileUser from './components/profile/ProfileUser';
import ProfileRestaurantUpdate from './components/profile/ProfileRestaurantUpdate';
import ViewUsers from './components/admin/ViewUsers';
import EditUser from './components/admin/EditUser';
import DeleteUser from './components/admin/DeleteUser';
import Actuator from "./components/admin/Actuator";


class App extends Component {


  render() {
    return (
      <Router>
        <div className="container">
          <MenuComponent/>
          <Switch>
            <Route path='/' exact component={Login} />
            <AuthenticatedRoute path='/createDish' exact component={AddDish} />
            <AuthenticatedRoute path='/dishes' exact component={ViewDatesOfDishes} />
            <AuthenticatedRoute path='/dish/edit/:id' component={EditDish} />
            <AuthenticatedRoute path='/dish/delete/:id' component={DeleteDish} />
            <AuthenticatedRoute path='/restaurants/view' exact component={ListRestaurantToday} />
            <Route path="/login" exact component={Login} />
            <Route path="/signUpUser" exact component={SignUpUser} />
            <Route path="/signUpCompany" exact component={SignUpCompany} />
            <Route path="/statistic" exact component={ViewDatesOfVotes} />
            <AuthenticatedRoute path="/profileRestaurant" exact component={ProfileRestaurant} />
            <AuthenticatedRoute path="/profileRestaurantUpdate" exact component={ProfileRestaurantUpdate} />
            <AuthenticatedRoute path='/restaurantPage' exact component={RestaurantPage} />
            <AuthenticatedRoute path='/user/profile' exact component={ProfileUser} />
            <AuthenticatedRoute path='/admin/users' exact component={ViewUsers} />
            <AuthenticatedRoute path='/admin/editUser/:id' exact component={EditUser} />
            <AuthenticatedRoute path='/admin/user/delete/:id' component={DeleteUser} />
            <AuthenticatedRoute path='/actuator' component={Actuator} />
          </Switch>
        </div>
      </Router>
    );
  }

}

export default App;
